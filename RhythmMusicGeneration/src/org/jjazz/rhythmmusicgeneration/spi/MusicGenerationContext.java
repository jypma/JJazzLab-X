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
package org.jjazz.rhythmmusicgeneration.spi;

import java.util.ArrayList;
import java.util.List;
import org.jjazz.midimix.MidiMix;
import org.jjazz.rhythm.api.Rhythm;
import org.jjazz.rhythm.api.RhythmVoice;
import org.jjazz.rhythmmusicgeneration.Utilities;
import org.jjazz.song.api.Song;
import org.jjazz.songstructure.api.SongPart;
import org.jjazz.util.Range;

/**
 * Information to be used by a Rhythm to generate music.
 * <p>
 * The class also provides some convenient methods to extract song data (e.g. song parts) relevant to the context defined by this
 * object.
 */
public class MusicGenerationContext
{

    private Song song;
    private MidiMix mix;
    private Range range;

    /**
     * Create a MusicGenerationContext object for the whole song.
     *
     * @param s
     * @param mix
     */
    public MusicGenerationContext(Song s, MidiMix mix)
    {
        this(s, mix, null);
    }

    /**
     * Create a MusicGenerationContext object for a whole or a part of the song.
     *
     * @param s
     * @param mix
     * @param r   If null, the range will represent the whole song from first to last bar.
     */
    public MusicGenerationContext(Song s, MidiMix mix, Range r)
    {
        if (s == null || mix == null)
        {
            throw new IllegalArgumentException("s=" + s + " mix=" + mix + "r=" + r);
        }
        song = s;
        this.mix = mix;
        int lastBar = s.getSongStructure().getSizeInBars() - 1;
        if (r == null)
        {
            this.range = new Range(0, lastBar);
        } else if (r.from > lastBar || r.to > lastBar)
        {
            throw new IllegalArgumentException("s=" + s + " mix=" + mix + "r=" + r);
        } else
        {
            this.range = r;
        }
    }

    /**
     * Music should be produced for this song.
     *
     * @return
     */
    public Song getSong()
    {
        return song;
    }

    /**
     * Music should be produced for this MidiMix.
     *
     * @return
     */
    public MidiMix getMidiMix()
    {
        return mix;
    }

    /**
     * Music should be produced for this range of bars
     *
     * @return
     */
    public Range getRange()
    {
        return range;
    }

    /**
     * Get all the song parts which are contained in this context.
     * <p>
     * See the contains() method.
     *
     * @return Can be empty.
     */
    public List<SongPart> getSongParts()
    {
        ArrayList<SongPart> res = new ArrayList<>();
        for (SongPart spt : song.getSongStructure().getSongParts())
        {
            if (contains(spt))
            {
                res.add(spt);
            }
        }
        return res;
    }

    /**
     * Get the Range of bars of spt belonging to this context.
     *
     * @param spt
     * @return Can be the VOID_RANGE if spt is not part of this context.
     */
    public Range getContainedSptBars(SongPart spt)
    {
        return spt.getRange().getIntersectRange(range);
    }

    /**
     *
     * @param spt
     * @return True if at least one bar of spt is in this context range.
     */
    public boolean contains(SongPart spt)
    {
        return spt.getRange().intersect(range);
    }

    /**
     * Get the list of unique rhythms used in this context.
     *
     * @return
     */
    public List<Rhythm> getUniqueRhythms()
    {
        ArrayList<Rhythm> res = new ArrayList<>();
        for (SongPart spt : song.getSongStructure().getSongParts())
        {
            if (contains(spt) && !res.contains(spt.getRhythm()))
            {
                res.add(spt.getRhythm());
            }
        }
        return res;
    }

    /**
     * Get the list of unique rhythm voices used in this context.
     *
     * @return
     */
    public List<RhythmVoice> getUniqueRhythmVoices()
    {
        ArrayList<RhythmVoice> rvs = new ArrayList<>();
        for (Rhythm r : getUniqueRhythms())
        {
            rvs.addAll(r.getRhythmVoices());
        }
        return rvs;
    }

    /**
     * The starting tick for the first bar of this SontPart which is in this context range.
     * <p>
     * Use MidiConst.PPQ_RESOLUTION ticks.
     *
     * @param spt
     * @return
     */
    public long getSptStartTick(SongPart spt)
    {
        List<SongPart> spts = getSongParts();
        if (!spts.contains(spt))
        {
            throw new IllegalArgumentException("spts=" + spts + " spt=" + spt);
        }
        long tick = 0;
        for (SongPart spti : spts)
        {
            if (spti == spt)
            {
                break;
            } else
            {
                int nbBars = range.getIntersectRange(spti.getRange()).size();
                tick += Utilities.getTickLength(spti, nbBars);
            }
        }
        return tick;
    }

    @Override
    public String toString()
    {
        return "MusicGenerationContext[song=" + song.getName() + ", midiMix=" + mix + ", range=" + range + "]";
    }
}
