/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *  Copyright @2019 Jerome Lelasseux. All rights reserved.
 *
 *  This file is part of the JJazzLabX software.
 *   
 *  JJazzLabX is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License (LGPLv3) 
 *  as published by the Free Software Foundation, either version 3 of the License, 
 *  or (at your option) any later version.
 *
 *  JJazzLabX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with JJazzLabX.  If not, see <https://www.gnu.org/licenses/>
 * 
 *  Contributor(s): 
 */
package org.jjazz.phrase.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import org.jjazz.harmony.api.Chord;
import org.jjazz.harmony.api.Note;
import org.jjazz.harmony.api.ScaleManager;
import org.jjazz.harmony.api.TimeSignature;
import org.jjazz.midi.api.MidiConst;
import org.jjazz.midi.api.MidiUtilities;
import org.jjazz.util.api.FloatRange;
import org.jjazz.util.api.LongRange;

/**
 * A list of NoteEvents sorted by start position.
 * <p>
 * Use addOrdered() to add a NoteEvent: this will ensure NoteEvents are kept ordered. Use of other add()/addAll() methods should
 * be used for optimization only when you are sure it will not break the NoteEvents order.
 * <p>
 * LinkedList implementation to speed up item insertion/remove rather than random access.
 */
public class Phrase extends LinkedList<NoteEvent> implements Serializable
{

    /**
     * NoteEvent client property set when new NoteEvents are created from existing ones.
     */
    public static final String PARENT_NOTE = "PARENT_NOTE";
    private final int channel;
    private static final Logger LOGGER = Logger.getLogger(Phrase.class.getSimpleName());

    /**
     *
     * @param channel
     */
    public Phrase(int channel)
    {
        if (!MidiConst.checkMidiChannel(channel))
        {
            throw new IllegalArgumentException("channel=" + channel);   //NOI18N
        }
        this.channel = channel;
    }


    /**
     * Add a clone of each p's events to this phrase.
     *
     * @param p
     */
    public void add(Phrase p)
    {
        for (NoteEvent mne : p)
        {
            addOrdered(mne.clone());
        }
    }

    /**
     * Overridden to throw UnsupportedOperationException, can't be used since NoteEvents are ordered by position.
     *
     * @param index
     * @param c
     * @return
     */
    @Override
    public boolean addAll​(int index, Collection<? extends NoteEvent> c)
    {
        throw new UnsupportedOperationException("Can't be used: notes are ordered by position");
    }

    /**
     * Add a collection of NoteEvents.
     * <p>
     * Overridden to rely on addOrdered().
     *
     * @param c
     * @return
     */
    @Override
    public boolean addAll(Collection<? extends NoteEvent> c)
    {
        c.forEach(ne -> addOrdered(ne));
        return !c.isEmpty();
    }

    /**
     * Add NoteEvents from a list of NOTE_ON/OFF Midi events at MidiConst.PPQ_RESOLUTION.
     * <p>
     * NOTE_ON events without a corresponding NOTE_OFF event are ignored.
     *
     * @param midiEvents       MidiEvents which are not ShortMessage.Note_ON/OFF are ignored. Must be ordered by tick position,
     *                         resolution must be MidiConst.PPQ_RESOLUTION.
     * @param posInBeatsOffset The position in natural beats of the first tick of the track.
     * @param ignoreChannel    If true, add also NoteEvents for MidiEvents which do not match this phrase channel.
     * @see MidiUtilities#getMidiEvents(javax.sound.midi.Track, java.util.function.Predicate, LongRange)
     * @see MidiConst#PPQ_RESOLUTION
     */
    public void add(List<MidiEvent> midiEvents, float posInBeatsOffset, boolean ignoreChannel)
    {

        // Build the NoteEvents
        MidiEvent[] lastNoteOn = new MidiEvent[128];
        for (MidiEvent me : midiEvents)
        {
            long tick = me.getTick();
            ShortMessage sm = MidiUtilities.getNoteShortMessage(me.getMessage());
            if (sm == null)
            {
                // It's not a note ON/OFF message
                continue;
            }

            int pitch = sm.getData1();
            int velocity = sm.getData2();
            int eventChannel = sm.getChannel();


            if (!ignoreChannel && channel != eventChannel)
            {
                // Different channel, ignore
                continue;
            }

            if (sm.getCommand() == ShortMessage.NOTE_ON && velocity > 0)
            {
                // NOTE_ON
                lastNoteOn[pitch] = me;

            } else
            {
                MidiEvent meOn = lastNoteOn[pitch];

                // NOTE_OFF
                if (meOn != null)
                {
                    // Create the NoteEvent
                    long tickOn = meOn.getTick();
                    ShortMessage smOn = (ShortMessage) meOn.getMessage();
                    float duration = ((float) tick - tickOn) / MidiConst.PPQ_RESOLUTION;
                    float posInBeats = posInBeatsOffset + ((float) tickOn / MidiConst.PPQ_RESOLUTION);
                    NoteEvent ne = new NoteEvent(pitch, duration, smOn.getData2(), posInBeats);
                    addOrdered(ne);

                    // Clean the last NoteOn
                    lastNoteOn[pitch] = null;
                } else
                {
                    // A note Off without a previous note On, do nothing
                }
            }
        }
    }

    /**
     * Add a NoteEvent at the correct index using NoteEvent natural ordering.
     * <p>
     * @param mne
     * @see NoteEvent#compareTo(org.jjazz.phrase.api.NoteEvent)
     */
    public void addOrdered(NoteEvent mne)
    {
        int res = Collections.binarySearch(this, mne);

        int index;
        if (res >= 0)
        {
            index = res;
            LOGGER.log(Level.FINE, "addOrdered() Inserting mne={0} but the same NoteEvent already exists at index={2}. this={1}", new Object[]
            {
                mne, this, index
            });
        } else
        {
            index = -(res + 1);
        }

        add(index, mne);
    }

    /**
     * A deep clone: returned phrase contains clones of the original NoteEvents.
     *
     * @return
     */
    @Override
    public Phrase clone()
    {
        return getFilteredPhrase(ne -> true);
    }

    /**
     *
     * @return 1-16
     */
    public int getChannel()
    {
        return channel;
    }

    /**
     * Get the beat range from start of first note to end of last note.
     *
     * @return FloatRange.EMPTY_FLOAT_RANGE if phrase is empty.
     */
    public FloatRange getBeatRange()
    {
        if (isEmpty())
        {
            return FloatRange.EMPTY_FLOAT_RANGE;
        }
        float startPos = isEmpty() ? 0 : getFirst().getPositionInBeats();
        NoteEvent lastNote = getLast();
        FloatRange fr = new FloatRange(startPos, lastNote.getPositionInBeats() + lastNote.getDurationInBeats());
        return fr;
    }

    /**
     *
     * @return Null if phrase is empty.
     */
    public NoteEvent getHighestPitchNote()
    {
        return stream().max(Comparator.comparing(NoteEvent::getPitch)).orElse(null);
    }

    /**
     *
     * @return Null if phrase is empty.
     */
    public NoteEvent getLowestPitchNote()
    {
        return stream().min(Comparator.comparing(NoteEvent::getPitch)).orElse(null);
    }

    /**
     *
     * @return Null if phrase is empty.
     */
    public NoteEvent getHighestVelocityNote()
    {
        return stream().max(Comparator.comparing(NoteEvent::getVelocity)).orElse(null);
    }

    /**
     *
     * @return 0 If phrase is empty.
     */
    public float getLastEventPosition()
    {
        return isEmpty() ? 0 : getLast().getPositionInBeats();
    }

    /**
     * Get a new phrase from the NoteEvents who match the specified predicate.
     * <p>
     * New phrase contains clones of the filtered NoteEvents.
     *
     * @param tester
     * @return
     */
    public Phrase getFilteredPhrase(Predicate<NoteEvent> tester)
    {
        Phrase res = new Phrase(channel);
        stream()
                .filter(tester)
                .forEach(ne -> res.add(ne.clone()));      // Don't need addOrdered here
        return res;
    }

    /**
     * Return a new Phrase with only filtered notes processed by the specified mapper.
     * <p>
     * Notes of the returned phrase will have their PARENT_NOTE client property set to:<br>
     * - source note's PARENT_NOTE client property if this property is not null, or<br>
     * - the source note from this phrase.
     *
     * @param tester
     * @param mapper
     * @return
     */
    public Phrase getFilteredAndMappedPhrase(Predicate<NoteEvent> tester, Function<NoteEvent, NoteEvent> mapper)
    {
        Phrase res = new Phrase(channel);
        for (NoteEvent ne : this)
        {
            if (tester.test(ne))
            {
                NoteEvent newNe = mapper.apply(ne);
                newNe.setClientProperties(ne);
                if (newNe.getClientProperty(PARENT_NOTE) == null)
                {
                    newNe.putClientProperty(PARENT_NOTE, ne);         // If no previous PARENT_NOTE client property we can add one
                }
                res.addOrdered(newNe);
            }
        }
        return res;
    }

    /**
     * Modify this phrase with filtered notes processed by the specified mapper.
     * <p>
     *
     * @param tester
     * @param mapper The mapper must NOT change the position
     */
    public void processEvents(Predicate<NoteEvent> tester, Function<NoteEvent, NoteEvent> mapper)
    {
        for (var it = listIterator(); it.hasNext();)
        {
            NoteEvent ne = it.next();
            if (tester.test(ne))
            {
                NoteEvent newNe = mapper.apply(ne);
                newNe.setClientProperties(ne);
                it.set(newNe);
            }
        }
    }

    /**
     * Get a new phrase with notes velocity changed.
     * <p>
     * Velocity is always maintained between 0 and 127. Notes of the returned phrase will have their PARENT_NOTE client property
     * set to:<br>
     * - source note's PARENT_NOTE client property if this property is not null, or<br>
     * - the source note from this phrase
     *
     * @param f A function modifying the velocity.
     * @return A new phrase
     */
    public Phrase getVelocityProcessedPhrase(Function<Integer, Integer> f)
    {
        return getFilteredAndMappedPhrase(ne -> true, ne ->
        {
            int v = MidiUtilities.limit(f.apply(ne.getVelocity()));
            NoteEvent newNe = new NoteEvent(ne, ne.getPitch(), ne.getDurationInBeats(), v);
            return newNe;
        });
    }

    /**
     * Change the velocity of all notes of this Phrase.
     * <p>
     * Velocity is always maintained between 0 and 127.
     *
     * @param f A function modifying the velocity.
     */
    public void processVelocity(Function<Integer, Integer> f)
    {
        processEvents(ne -> true, ne ->
        {
            int v = MidiUtilities.limit(f.apply(ne.getVelocity()));
            NoteEvent newNe = new NoteEvent(ne, ne.getPitch(), ne.getDurationInBeats(), v);
            return newNe;
        });
    }

    /**
     * Get a new phrase with all notes changed.
     * <p>
     * Pitch is always maintained between 0 and 127. Notes of the returned phrase will have their PARENT_NOTE client property set
     * to:<br>
     * - source note's PARENT_NOTE client property if this property is not null, or<br>
     * - the source note from this phrase
     *
     * @param f A function modifying the pitch.
     * @return A new phrase
     */
    public Phrase getPitchProcessedPhrase(Function<Integer, Integer> f)
    {
        return getFilteredAndMappedPhrase(ne -> true, ne ->
        {
            int p = MidiUtilities.limit(f.apply(ne.getPitch()));
            NoteEvent newNe = new NoteEvent(ne, p, ne.getDurationInBeats(), ne.getVelocity());
            return newNe;
        });
    }

    /**
     * Change the pitch of all notes of this Phrase.
     * <p>
     * Pitch is always maintained between 0 and 127.
     *
     * @param f A function modifying the pitch.
     */
    public void processPitch(Function<Integer, Integer> f)
    {
        processEvents(ne -> true, ne ->
        {
            int p = MidiUtilities.limit(f.apply(ne.getPitch()));
            NoteEvent newNe = new NoteEvent(ne, p, ne.getDurationInBeats(), ne.getVelocity());
            return newNe;
        });
    }

    /**
     * Make sure there is no note ringing after the specified position.
     * <p>
     * Notes starting after posInBeats are removed.<br>
     * If a note starts before posInBeats but is still ON beyond posInBeats, note duration is shortened to have Note OFF at
     * posInBeats.
     *
     * @param posInBeats
     */
    public void silenceAfter(float posInBeats)
    {
        // Use an iterator to avoid using get(i) which is O(n) for a linkedlist
        ListIterator<NoteEvent> it = listIterator(size());
        while (it.hasPrevious())
        {
            NoteEvent ne = it.previous();
            float pos = ne.getPositionInBeats();
            if (pos >= posInBeats)
            {
                // Remove notes after posInBeats
                it.remove();
            } else if (pos + ne.getDurationInBeats() > posInBeats)
            {
                // Shorten notes before posInBeats but ending after posInBeats
                float newDuration = posInBeats - pos;
                NoteEvent ne2 = new NoteEvent(ne, newDuration);
                it.set(ne2);
            }
        }
    }


    /**
     * Get a new phrase which keeps only the notes in the specified beat range, taking into account possible
     * live-played/non-quantized notes via the beatWindow parameter.
     * <p>
     * First, if beatWindow &gt; 0 then notes starting in the range [range.from-beatWindow; range.from[ are changed in the
     * returned phrase so they start at range.from, and notes starting in the range [range.to-beatWindow; range.to[ are removed.
     * <p>
     * Then, if a note is starting before startPos and ending after range.from: <br>
     * - if keepLeft is false, the note is removed<br>
     * - if keepLeft is true, the note is replaced by a shorter identical one starting at range.from
     * <p>
     * If a note is starting before range.to and ending after range.to: <br>
     * - if cutRight == 0 the note is not removed.<br>
     * - if cutRight == 1, the note is replaced by a shorter identical that ends at range.to.<br>
     * - if cutRight == 2, the note is removed<br>
     * <p>
     *
     * @param range
     * @param keepLeft
     * @param cutRight
     * @param beatWindow A tolerance window if this phrase contains live-played/non-quantized notes. Typical value is 0.1f.
     * @return
     * @see #silence(org.jjazz.util.api.FloatRange, boolean, boolean)
     */
    public Phrase getSlice(FloatRange range, boolean keepLeft, int cutRight, float beatWindow)
    {
        checkArgument(cutRight >= 0 && cutRight <= 2, "cutRight=%s", cutRight);
        checkArgument(beatWindow >= 0);

        Phrase res = new Phrase(channel);


        // Preprocess to accomodate for live playing / non-quantized notes
        List<NoteEvent> beatWindowProcessedNotes = new ArrayList<>();
        if (beatWindow > 0)
        {
            FloatRange frLeft = range.from - beatWindow > 0 ? new FloatRange(range.from - beatWindow, range.from) : null;
            FloatRange frRight = new FloatRange(range.to - beatWindow, range.to);

            ListIterator<NoteEvent> it = listIterator();
            while (it.hasNext())
            {
                var ne = it.next();
                var neBr = ne.getBeatRange();
                if (frLeft != null && frLeft.contains(neBr.from, true))
                {
                    if (frLeft.contains(neBr, false))
                    {
                        // Note is fully contained in the beatWindow! Probably a drums/perc note, move it
                        NoteEvent newNe = new NoteEvent(ne, ne.getDurationInBeats(), range.from);
                        res.addOrdered(newNe);
                    } else
                    {
                        // Note crosses range.from, make it start at range.from
                        float newDur = Math.max(neBr.to - range.from, 0.05f);
                        NoteEvent newNe = new NoteEvent(ne, newDur, range.from);
                        res.addOrdered(newNe);
                    }
                    beatWindowProcessedNotes.add(ne);

                } else if (frRight.contains(neBr.from, true))
                {
                    // Remove the note
                    beatWindowProcessedNotes.add(ne);
                }
            }

        }


        // 
        ListIterator<NoteEvent> it = listIterator();
        while (it.hasNext())
        {
            NoteEvent ne = it.next();


            if (beatWindowProcessedNotes.contains(ne))
            {
                // It's already processed, skip
                continue;
            }

            float nePosFrom = ne.getPositionInBeats();
            float nePosTo = nePosFrom + ne.getDurationInBeats();


            if (nePosFrom < range.from)
            {
                // It starts before the slice zone, don't add, except if it overlaps the slice zone
                if (keepLeft && nePosTo > range.from)
                {
                    // It even goes beyond the slice zone!                                        
                    if (nePosTo > range.to)
                    {
                        switch (cutRight)
                        {
                            case 0:
                                // Add it but don't change its end point
                                break;
                            case 1:
                                // Make it shorter
                                nePosTo = range.to;
                                break;
                            case 2:
                                // Do not add
                                continue;
                            default:
                                throw new IllegalStateException("cutRight=" + cutRight);
                        }
                    }
                    float newDur = nePosTo - range.from;
                    NoteEvent newNe = new NoteEvent(ne, newDur, range.from);
                    res.addOrdered(newNe);
                }
            } else if (nePosFrom < range.to)
            {
                // It starts in the slice zone, add it
                if (nePosTo <= range.to)
                {
                    // It ends in the slice zone, easy
                    res.addOrdered(ne);
                } else
                {
                    // It goes beyond the slice zone
                    switch (cutRight)
                    {
                        case 0:
                            // Add it anyway
                            res.addOrdered(ne);
                            break;
                        case 1:
                            // Add it but make it shorter
                            float newDur = range.to - nePosFrom;
                            NoteEvent newNe = new NoteEvent(ne, newDur);
                            res.addOrdered(newNe);
                            break;
                        case 2:
                            // Do not add
                            break;
                        default:
                            throw new IllegalStateException("cutRight=" + cutRight);
                    }
                }
            } else
            {
                // It starts after the slice zone, do nothing
            }
        }


        return res;
    }


    /**
     * Remove all notes whose start position is in the specified beat range, taking into account possible
     * live-played/non-quantized notes via the beatWindow parameter.
     * <p>
     * If a note is starting before range.from and ending after range.from: <br>
     * - if cutLeft is false, the note is not removed.<br>
     * - if cutLeft is true, the note is replaced by a shorter identical that ends at range.from, except if the note starts in the
     * range [range.from-beatWindow;range.from[, then it's removed.<p>
     * If a note is starting before range.to and ending after range.to: <br>
     * - if keepRight is false, the note is removed, except if the note starts in the range [range.to-beatWindow;range.to[, then
     * it's replaced by a shorter identical one starting at range<br>
     * - if keepRight is true, the note is replaced by a shorter identical one starting at range.to<br>
     *
     * @param range
     * @param cutLeft
     * @param keepRight
     * @param beatWindow A tolerance window if this phrase contains live-played/non-quantized notes. Typical value is 0.1f.
     * @see #getSlice(org.jjazz.util.api.FloatRange, boolean, int, float)
     */
    public void silence(FloatRange range, boolean cutLeft, boolean keepRight, float beatWindow)
    {
        checkArgument(beatWindow >= 0);

        ArrayList<NoteEvent> toBeAdded = new ArrayList<>();

        FloatRange frLeft = FloatRange.EMPTY_FLOAT_RANGE;
        FloatRange frRight = FloatRange.EMPTY_FLOAT_RANGE;

        if (beatWindow > 0)
        {
            frLeft = range.from - beatWindow >= 0 ? new FloatRange(range.from - beatWindow, range.from) : FloatRange.EMPTY_FLOAT_RANGE;
            frRight = range.to - beatWindow >= range.from ? new FloatRange(range.to - beatWindow, range.to) : FloatRange.EMPTY_FLOAT_RANGE;
        }


        ListIterator<NoteEvent> it = listIterator();
        while (it.hasNext())
        {
            NoteEvent ne = it.next();
            float nePosFrom = ne.getPositionInBeats();
            float nePosTo = nePosFrom + ne.getDurationInBeats();

            if (nePosFrom < range.from)
            {
                if (nePosTo <= range.from)
                {
                    // Leave note unchanged

                } else if (cutLeft)
                {
                    // Replace the note by a shorter one, except if it's in the frLeft beat window
                    if (!frLeft.contains(nePosFrom, true))
                    {

                        // Replace
                        float newDur = range.from - nePosFrom;
                        NoteEvent newNe = new NoteEvent(ne, newDur, nePosFrom);
                        it.set(newNe);


                        // Special case if note was extending beyond range.to and keepRight is true, add a note after range
                        if (keepRight && nePosTo > range.to)
                        {
                            newDur = nePosTo - range.to;
                            newNe = new NoteEvent(ne, newDur, range.to);
                            toBeAdded.add(newNe);
                        }
                    } else
                    {
                        // It's in the left beat window, directly remove the note
                        it.remove();
                    }

                }
            } else if (nePosFrom < range.to)
            {
                // Remove the note
                it.remove();

                // Re-add a note after range if required
                if (nePosTo > range.to && (keepRight || frRight.contains(nePosFrom, true)))
                {
                    float newDur = nePosTo - range.to;
                    NoteEvent newNe = new NoteEvent(ne, newDur, range.to);
                    toBeAdded.add(newNe);
                }
            } else
            {
                // nePosFrom is after range.to
                // Nothing
            }
        }

        // Add the new NoteEvents after range
        for (NoteEvent ne : toBeAdded)
        {
            addOrdered(ne);
        }
    }

    /**
     * Get the NoteEvents which match the tester and whose start position is in the [posFrom:posTo] or [posFrom:posTo[ range.
     *
     * @param tester
     * @param range
     * @param excludeUpperBound
     * @return
     */
    public List<NoteEvent> getNotes(Predicate<NoteEvent> tester, FloatRange range, boolean excludeUpperBound)
    {
        var res = new ArrayList<NoteEvent>();
        for (NoteEvent ne : this)
        {
            if (tester.test(ne) && range.contains(ne.getPositionInBeats(), excludeUpperBound))
            {
                res.add(ne);
            }
            if (ne.getPositionInBeats() > range.to)
            {
                break;
            }
        }
        return res;
    }

    /**
     * Get the notes still ringing at specified position.
     * <p>
     *
     * @param posInBeats
     * @param strict     If true, notes starting or ending at posInBeats are excluded.
     * @return The list of notes whose startPos is before (or equals) posInBeats and range.to eafter (or equals) posInBeats
     */
    public List<NoteEvent> getCrossingNotes(float posInBeats, boolean strict)
    {
        ArrayList<NoteEvent> res = new ArrayList<>();
        var it = listIterator();
        while (it.hasNext())
        {
            NoteEvent ne = it.next();
            float pos = ne.getPositionInBeats();
            if ((strict && pos >= posInBeats) || (!strict && pos > posInBeats))
            {
                break;
            }
            if ((strict && pos + ne.getDurationInBeats() > posInBeats) || (!strict && pos + ne.getDurationInBeats() >= posInBeats))
            {
                res.add(ne);
            }
        }
        return res;
    }

    /**
     * Create MidiEvents for each note and add it to the specified track.
     * <p>
     * Tick resolution used is MidiConst.PPQ_RESOLUTION.
     *
     * @param track
     */
    public void fillTrack(Track track)
    {
        toMidiEvents().forEach(me -> track.add(me));
    }

    /**
     * Get all the phrase notes as MidiEvents.
     * <p>
     * Tick resolution used is MidiConst.PPQ_RESOLUTION.
     *
     * @return Each note is converted into 1 MidiEvent for note ON, 1 for the note OFF
     */
    public List<MidiEvent> toMidiEvents()
    {
        List<MidiEvent> res = new ArrayList<>();
        for (NoteEvent ne : this)
        {
            for (MidiEvent me : ne.toMidiEvents(channel))
            {
                res.add(me);
            }
        }
        return res;
    }

    /**
     * Replace all events by the same events but with position shifted.
     *
     * @param shiftInBeats The value added to each event's position.
     * @throws IllegalArgumentException If an event's position become negative.
     */
    public void shiftEvents(float shiftInBeats)
    {
        var it = listIterator();
        while (it.hasNext())
        {
            NoteEvent ne = it.next();
            float newPosInBeats = ne.getPositionInBeats() + shiftInBeats;
            if (newPosInBeats < 0)
            {
                throw new IllegalArgumentException("ne=" + ne + " shiftInBeats=" + shiftInBeats);   //NOI18N
            }
            NoteEvent shiftedNe = new NoteEvent(ne, ne.getDurationInBeats(), newPosInBeats);
            it.set(shiftedNe);
        }
    }

    /**
     * Get a chord made of all unique pitch NoteEvents present in the phrase.
     *
     * @return
     */
    public Chord getChord()
    {
        Chord c = new Chord(this);
        return c;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Phrase[ch=").append(channel).append("] size=").append(size()).append(" notes=").append(super.toString());
        return sb.toString();
    }

    public void dump()
    {
        LOGGER.info(toString());   //NOI18N
        for (NoteEvent ne : this)
        {
            LOGGER.info(ne.toString());   //NOI18N
        }
    }

    /**
     * Get notes matching the specified tester and return them per pitch.
     *
     * @param tester
     * @return The matching notes grouped per pitch.
     */
    public Map<Integer, List<NoteEvent>> getNotesPerPitch(Predicate<NoteEvent> tester)
    {
        var resMap = new HashMap<Integer, List<NoteEvent>>();

        for (var ne : this)
        {
            if (tester.test(ne))
            {
                List<NoteEvent> nes = resMap.get(ne.getPitch());
                if (nes == null)
                {
                    nes = new ArrayList<>();
                    resMap.put(ne.getPitch(), nes);
                }
                nes.add(ne);
            }
        }

        return resMap;
    }


    /**
     * Remove overlapped notes with identical pitch.
     * <p>
     * A note N1 is overlapped by N2 if N1's noteOn event occurs after N2's noteOn event and N1's noteOff event occurs before N2's
     * noteOff event.
     */
    public void removeOverlappedNotes()
    {
        // Get all the notes grouped per pitch
        HashMap<Integer, List<NoteEvent>> mapPitchNotes = new HashMap<>();
        for (NoteEvent ne : this)
        {
            int pitch = ne.getPitch();
            List<NoteEvent> nes = mapPitchNotes.get(pitch);
            if (nes == null)
            {
                nes = new ArrayList<>();
                mapPitchNotes.put(pitch, nes);
            }
            nes.add(ne);
        }

        // Search for overlapped notes
        HashSet<NoteEvent> overlappedNotes = new HashSet<>();
        for (Integer pitch : mapPitchNotes.keySet())
        {
            List<NoteEvent> notes = mapPitchNotes.get(pitch);
            if (notes.size() == 1)
            {
                continue;
            }
            ArrayList<NoteEvent> noteOnBuffer = new ArrayList<>();
            for (NoteEvent ne : notes)
            {
                FloatRange fr = ne.getBeatRange();
                boolean removed = false;
                Iterator<NoteEvent> itOn = noteOnBuffer.iterator();
                while (itOn.hasNext())
                {
                    NoteEvent noteOn = itOn.next();
                    FloatRange frOn = noteOn.getBeatRange();
                    if (frOn.to < fr.from)
                    {
                        // Remove noteOns which are now Off
                        itOn.remove();
                    } else if (frOn.to >= fr.to)
                    {
                        // Cur note is overlapped !
                        overlappedNotes.add(ne);
                        removed = true;
                        break;
                    }
                }
                if (!removed)
                {
                    noteOnBuffer.add(ne);
                }
            }
        }

        // Now remove the notes
        removeAll(overlappedNotes);
    }

    /**
     * Change the octave of notes whose pitch is above highLimit or below lowLimit.
     * <p>
     * Fixed new notes's PARENT_NOTE client property is preserved.
     *
     * @param lowLimit  There must be at least 1 octave between lowLimit and highLimit
     * @param highLimit There must be at least 1 octave between lowLimit and highLimit
     */
    public void limitPitch(int lowLimit, int highLimit)
    {
        if (lowLimit < 0 || highLimit > 127 || lowLimit > highLimit || highLimit - lowLimit < 11)
        {
            throw new IllegalArgumentException("lowLimit=" + lowLimit + " highLimit=" + highLimit);   //NOI18N
        }
        var it = listIterator();
        while (it.hasNext())
        {
            NoteEvent ne = it.next();
            int pitch = ne.getPitch();
            while (pitch < lowLimit)
            {
                pitch += 12;
            }
            while (pitch > highLimit)
            {
                pitch -= 12;
            }
            NoteEvent newNe = new NoteEvent(ne, pitch);
            it.set(newNe);
        }
    }

    /**
     * Compare the specified phrase with this phrase, but tolerate slight differences in position and duration.
     *
     *
     * @param p
     * @param nearWindow Used to compare NoteEvents position and duration.
     * @return
     * @see NoteEvent#equalsNearPosition(org.jjazz.phrase.api.NoteEvent, float)
     */
    public boolean equalsNearPosition(Phrase p, float nearWindow)
    {
        checkNotNull(p);
        if (size() != p.size())
        {
            return false;
        }
        Iterator<NoteEvent> pIt = p.iterator();
        for (NoteEvent ne : this)
        {
            if (!pIt.next().equalsNearPosition(ne, nearWindow))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Save the specified Phrase as a string.
     * <p>
     * Example "[8|NoteEventStr0|NoteEventStr1]" means a Phrase for channel 8 with 2 NoteEvents.
     *
     * @param p
     * @return
     * @see loadAsString(String)
     */
    static public String saveAsString(Phrase p)
    {
        StringJoiner joiner = new StringJoiner("|", "[", "]");
        joiner.add(String.valueOf(p.getChannel()));
        p.forEach(ne -> joiner.add(NoteEvent.saveAsString(ne)));
        return joiner.toString();
    }

    /**
     * Create a Phrase from the specified string.
     * <p>
     *
     * @param s
     * @return
     * @throws ParseException If s is not a valid string.
     * @see saveAsString(Phrase)
     */
    static public Phrase loadAsString(String s) throws ParseException
    {
        Phrase p = null;
        if (s.length() >= 4 && s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']')    // minimum string is e.g. [2|]
        {
            String[] strs = s.substring(1, s.length() - 1).split("\\|");
            try
            {
                int channel = Integer.parseInt(strs[0]);
                p = new Phrase(channel);
                for (int i = 1; i < strs.length; i++)
                {
                    NoteEvent ne = NoteEvent.loadAsString(strs[i]);
                    p.addOrdered(ne);
                }
            } catch (IllegalArgumentException | ParseException ex)       // Will catch NumberFormatException too
            {
                // Nothing
                LOGGER.warning("loadAsString() Catched ex=" + ex.getMessage());
            }

        }

        if (p == null)
        {
            throw new ParseException("Phrase.loadAsString() Invalid Phrase string s=" + s, 0);
        }
        return p;
    }


    /**
     * Build a Phrase with 12 notes C-D-E-F-G-A-B-C that fit into nbBeats.
     *
     * @param channel
     * @param startPos Position of the 1st note 'C'
     * @param nbBeats
     * @return
     */
    static public Phrase getCscalePhrase(int channel, float startPos, float nbBeats)
    {
        Phrase p = new Phrase(channel);
        float noteDur = nbBeats / 8f;
        float pos = startPos;
        for (Note n : ScaleManager.MAJOR.getNotes())
        {
            NoteEvent ne = new NoteEvent(n.getPitch() + 60, noteDur, n.getVelocity(), pos);
            pos += noteDur;
            p.addOrdered(ne);
        }
        // Add octave note at this end
        NoteEvent ne = new NoteEvent(72, noteDur, Note.VELOCITY_STD, pos);
        p.addOrdered(ne);
        return p;
    }

    /**
     * Get a phrase with random notes at random positions.
     *
     * @param channel
     * @param nbBars  Number of 4/4 bars.
     * @param nbNotes Number of random notes to generate.
     * @return
     */
    static public Phrase getRandomPhrase(int channel, int nbBars, int nbNotes)
    {
        Phrase p = new Phrase(channel);

        for (int i = 0; i < nbNotes; i++)
        {
            int pitch = (int) (40 + Math.round(20 * Math.random()));
            int vel = (int) (50 + Math.round(20 * Math.random()));
            float pos = Math.max(0, Math.round(nbBars * 4 * Math.random()) - 2);
            float dur = Math.random() > 0.5d ? 0.5f : 1f;
            p.addOrdered(new NoteEvent(pitch, dur, vel, pos));
        }

        return p;
    }

    /**
     * Get a basic drums phrase.
     *
     * @param startPosInBeats
     * @param nbBars
     * @param ts
     * @param channel         The channel of the returned phrase
     * @return
     */
    static public Phrase getBasicDrumPhrase(float startPosInBeats, int nbBars, TimeSignature ts, int channel)
    {
        if (ts == null || !MidiConst.checkMidiChannel(channel))
        {
            throw new IllegalArgumentException("nbBars=" + nbBars + " ts=" + ts + " channel=" + channel);   //NOI18N
        }
        Phrase p = new Phrase(channel);
        float duration = 0.25f;
        for (int bar = 0; bar < nbBars; bar++)
        {
            for (int beat = 0; beat < ts.getNbNaturalBeats(); beat++)
            {
                // 2 Hi Hat per beat
                NoteEvent ne = new NoteEvent(MidiConst.CLOSED_HI_HAT, duration, 80, startPosInBeats);
                p.addOrdered(ne);
                ne = new NoteEvent(MidiConst.CLOSED_HI_HAT, duration, 80, startPosInBeats + 0.5f);
                p.addOrdered(ne);

                // Bass drums or Snare
                int pitch;
                int velocity = 70;
                switch (beat)
                {
                    case 0:
                        pitch = MidiConst.ACOUSTIC_BASS_DRUM;
                        velocity = 120;
                        break;
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        pitch = MidiConst.ACOUSTIC_SNARE;
                        break;
                    default:
                        pitch = MidiConst.ACOUSTIC_BASS_DRUM;
                }
                ne = new NoteEvent(pitch, duration, velocity, startPosInBeats);
                p.addOrdered(ne);

                // Next beat
                startPosInBeats++;
            }
        }
        return p;
    }


    /**
     * Parse all tracks to build one phrase per used channel.
     * <p>
     * A track can use notes from different channels. Notes from a given channel can be on several tracks.
     *
     * @param tracksPPQ The Midi PPQ resolution (pulses per quarter) used in the tracks.
     * @param tracks
     * @param channels  Get phrases only for the specified channels. If empty, get phrases for all channels.
     * @return
     */
    static public List<Phrase> getPhrases(int tracksPPQ, Track[] tracks, Integer... channels)
    {
        Map<Integer, Phrase> mapRes = new HashMap<>();
        var selectedChannels = channels.length > 0 ? Arrays.asList(channels) : Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        for (Track track : tracks)
        {
            // Get all the events at the appropriate resolution
            var trackEvents = MidiUtilities.getMidiEvents(track,
                    ShortMessage.class,
                    sm -> sm.getCommand() == ShortMessage.NOTE_OFF || sm.getCommand() == ShortMessage.NOTE_ON,
                    null);
            trackEvents = MidiUtilities.getMidiEventsAtPPQ(trackEvents, tracksPPQ, MidiConst.PPQ_RESOLUTION);


            for (int channel : MidiUtilities.getUsedChannels(track))
            {
                if (selectedChannels.contains(channel))
                {
                    Phrase p = mapRes.get(channel);
                    if (p == null)
                    {
                        p = new Phrase(channel);
                        mapRes.put(channel, p);
                    }
                    p.add(trackEvents, 0, false);
                }
            }
        }

        return new ArrayList<>(mapRes.values());
    }


    // --------------------------------------------------------------------- 
    // Serialization
    // --------------------------------------------------------------------- */
    private Object writeReplace()
    {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException
    {
        throw new InvalidObjectException("Serialization proxy required");


    }


    /**
     * Rely on loadFromString()/saveAsString() methods.
     */
    private static class SerializationProxy implements Serializable
    {

        private static final long serialVersionUID = -1823649110L;

        private final int spVERSION = 1;
        private final String spSaveString;

        private SerializationProxy(Phrase p)
        {
            spSaveString = saveAsString(p);
        }

        private Object readResolve() throws ObjectStreamException
        {
            Phrase p;
            try
            {
                p = loadAsString(spSaveString);
            } catch (ParseException ex)
            {
                throw new InvalidObjectException(ex.getMessage());
            }
            return p;
        }
    }
}
