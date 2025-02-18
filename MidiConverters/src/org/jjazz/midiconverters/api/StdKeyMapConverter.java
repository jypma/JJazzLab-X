package org.jjazz.midiconverters.api;

import java.util.logging.Logger;
import org.jjazz.midi.api.keymap.KeyMapGSGM2;
import org.jjazz.midi.api.keymap.KeyMapXG;
import org.jjazz.midi.api.keymap.KeyMapGM;
import org.jjazz.midiconverters.spi.KeyMapConverter;
import org.jjazz.midi.api.DrumKit;
import org.jjazz.midi.api.keymap.KeyMapXG_PopLatin;

/**
 * Note mapping between GSGM2/XG/GM DrumMaps.
 */
public class StdKeyMapConverter implements KeyMapConverter
{

    private static StdKeyMapConverter INSTANCE;
    private static final Logger LOGGER = Logger.getLogger(StdKeyMapConverter.class.getSimpleName());
    private final KeyMapGM gmMap;
    private final KeyMapGSGM2 gm2Map;
    private final KeyMapXG xgMap;
    private final KeyMapXG_PopLatin xgLatinMap;

    public static StdKeyMapConverter getInstance()
    {
        synchronized (StdKeyMapConverter.class)
        {
            if (INSTANCE == null)
            {
                INSTANCE = new StdKeyMapConverter();
            }
        }
        return INSTANCE;
    }

    private StdKeyMapConverter()
    {
        gmMap = KeyMapGM.getInstance();
        gm2Map = KeyMapGSGM2.getInstance();
        xgMap = KeyMapXG.getInstance();
        xgLatinMap = KeyMapXG_PopLatin.getInstance();

    }

    @Override
    public String getConverterId()
    {
        return "StdMapper";
    }

    @Override
    public String toString()
    {
        return getConverterId();
    }

    @Override
    public boolean accept(DrumKit.KeyMap srcMap, DrumKit.KeyMap destMap)
    {
        if (srcMap.equals(destMap))
        {
            return true;
        }
        boolean b = false;
        if (srcMap == gmMap)
        {
            b = destMap != xgLatinMap;
        } else if (srcMap == xgMap)
        {
            b = destMap != xgLatinMap;
        } else if (srcMap == xgLatinMap)
        {
            b = true;
        } else if (srcMap == gm2Map)
        {
            b = destMap != xgLatinMap;
        }
        return b;
    }

    @Override
    public int convertKey(DrumKit.KeyMap srcMap, int srcPitch, DrumKit.KeyMap destMap)
    {
        if (srcPitch < 0 || srcPitch > 127)
        {
            throw new IllegalArgumentException("srcMap=" + srcMap + " srcPitch=" + srcPitch + " destMap=" + destMap);   //NOI18N
        }
        
        if (srcMap.getKeyName(srcPitch) == null)
        {
            return -1;
        }

        if (!accept(srcMap, destMap))
        {
            return -1;
        }

        if (srcMap.equals(destMap))
        {
            return srcPitch;
        }

        // By default no change
        int destPitch = srcPitch;

        if (srcMap == xgMap)
        {
            if (destMap == gmMap)
            {
                // XG => GM
                destPitch = convertPitch(srcPitch, xgMap, MAP_XG_TO_GM);
            } else if (destMap == gm2Map)
            {
                // XG => GM2
                destPitch = convertPitch(srcPitch, xgMap, MAP_XG_TO_GM2);
            }
        } else if (srcMap == xgLatinMap)
        {
            if (destMap == gmMap)
            {
                // XGLatin => GM
                destPitch = convertPitch(srcPitch, xgLatinMap, MAP_XGLATIN_TO_GM);
            } else if (destMap == gm2Map)
            {
                // XGLatin => GM2
                destPitch = convertPitch(srcPitch, xgLatinMap, MAP_XGLATIN_TO_GM2);
            } else if (destMap == xgMap)
            {
                // XGLatin => XG
                destPitch = convertPitch(srcPitch, xgLatinMap, MAP_XGLATIN_TO_XG);
            }
        } else if (srcMap == gm2Map)
        {
            if (destMap == gmMap)
            {
                // GM2 => GM
                destPitch = convertPitch(srcPitch, gm2Map, MAP_GM2_TO_GM);
            } else if (destMap == xgMap)
            {
                // GM2 => XG
                destPitch = convertPitch(srcPitch, gm2Map, MAP_GM2_TO_XG);
            }
        }
        return destPitch;
    }

    public boolean isStandardKeyMap(DrumKit.KeyMap map)
    {
        if (map == null)
        {
            throw new NullPointerException("map");   //NOI18N
        }
        return map == xgMap || map == gm2Map || map == gmMap || map == xgLatinMap;
    }

    // =========================================================================
    // Private methods
    // =========================================================================
    private int convertPitch(int srcPitch, DrumKit.KeyMap srcKeyMap, int[] mapSrcDest)
    {
        int p = srcPitch - srcKeyMap.getRange().lowNote;
        if (p < 0 || p >= mapSrcDest.length)
        {
            LOGGER.warning("convertPitch() Invalid srcPitch/keyMap: srcPitch=" + srcPitch + " srcKeyMap=" + srcKeyMap + " mapSrcDest.length=" + mapSrcDest.length);   //NOI18N
            return srcPitch;
        }
        return mapSrcDest[p];
    }

    final private int[] MAP_XG_TO_GM =
    {
        63,
        62,
        69,
        60,
        76,
        77,
        76,
        37,
        76,
        77,
        76,
        77,
        69,
        70,
        69,
        70,
        25,
        75,
        65,
        75,
        35,
        37,
        35,
        36,
        37,
        38,
        39,
        40,
        41,
        42,
        43,
        44,
        45,
        46,
        47,
        48,
        49,
        50,
        51,
        52,
        53,
        54,
        55,
        56,
        57,
        58,
        59,
        60,
        61,
        62,
        63,
        64,
        65,
        66,
        67,
        68,
        69,
        70,
        71,
        72,
        73,
        74,
        75,
        76,
        77,
        78,
        79,
        80,
        81,
        69,
        53,
        59
    };

    final private int[] MAP_XG_TO_GM2 =
    {
        86,
        87,
        27,
        28,
        29,
        30,
        26,
        32,
        33,
        34,
        33,
        34,
        69,
        70,
        69,
        70,
        25,
        85,
        65,
        31,
        41,
        37,
        35,
        36,
        37,
        38,
        39,
        40,
        41,
        42,
        43,
        44,
        45,
        46,
        47,
        48,
        49,
        50,
        51,
        52,
        53,
        54,
        55,
        56,
        57,
        58,
        59,
        60,
        61,
        62,
        63,
        64,
        65,
        66,
        67,
        68,
        69,
        70,
        71,
        72,
        73,
        74,
        75,
        76,
        77,
        78,
        79,
        80,
        81,
        82,
        83,
        84
    };

    final private int[] MAP_XGLATIN_TO_GM2 =
    {
        87,
        86,
        62,
        76,
        77,
        39,
        26,
        85,
        60,
        61,
        63,
        62,
        63,
        64,
        62,
        61,
        60,
        64,
        63,
        62,
        62,
        64,
        60,
        60,
        63,
        62,
        66,
        65,
        61,
        61,
        62,
        63,
        65,
        66,
        45,
        69,
        70,
        69,
        70,
        69,
        48,
        48,
        69,
        70,
        69,
        70,
        50,
        56,
        76,
        56,
        77,
        73,
        74,
        73,
        74,
        54,
        54,
        70,
        69,
        70,
        82,
        69,
        78,
        79,
        56,
        56,
        69,
        70,
        80,
        81,
        69,
        84
    };

    final private int[] MAP_XGLATIN_TO_GM =
    {
        35,
        38,
        44,
        76,
        77,
        39,
        77,
        75,
        60,
        61,
        63,
        62,
        63,
        64,
        62,
        61,
        60,
        64,
        63,
        62,
        62,
        64,
        60,
        60,
        63,
        62,
        61,
        64,
        61,
        61,
        62,
        63,
        61,
        64,
        45,
        69,
        70,
        69,
        70,
        69,
        48,
        48,
        69,
        70,
        69,
        70,
        50,
        56,
        76,
        56,
        77,
        73,
        74,
        73,
        74,
        54,
        54,
        70,
        69,
        70,
        70,
        69,
        78,
        79,
        76,
        77,
        69,
        70,
        80,
        81,
        69,
        51
    };

    final private int[] MAP_GM2_TO_GM =
    {
        75,
        75,
        38,
        38,
        76,
        69,
        60,
        76,
        77,
        76,
        37,
        76,
        77,
        35,
        36,
        37,
        38,
        39,
        40,
        41,
        42,
        43,
        44,
        45,
        46,
        47,
        48,
        49,
        50,
        51,
        52,
        53,
        54,
        55,
        56,
        57,
        58,
        59,
        60,
        61,
        62,
        63,
        64,
        65,
        66,
        67,
        68,
        69,
        70,
        71,
        72,
        73,
        74,
        75,
        76,
        77,
        78,
        79,
        80,
        81,
        69,
        53,
        59,
        75,
        43,
        45
    };

    final private int[] MAP_XGLATIN_TO_XG =
    {
        14,
        13,
        13,
        76,
        77,
        39,
        19,
        30,
        60,
        61,
        63,
        62,
        63,
        64,
        62,
        61,
        60,
        64,
        63,
        62,
        62,
        64,
        60,
        60,
        63,
        62,
        66,
        65,
        61,
        61,
        62,
        63,
        65,
        66,
        45,
        69,
        70,
        69,
        70,
        69,
        48,
        48,
        69,
        70,
        69,
        70,
        50,
        56,
        76,
        56,
        77,
        73,
        74,
        73,
        74,
        54,
        54,
        70,
        69,
        70,
        82,
        69,
        78,
        79,
        56,
        56,
        69,
        70,
        80,
        81,
        69,
        84
    };

    private static final int[] MAP_GM2_TO_XG =
    {
        21,
        22,
        31,
        29,
        19,
        15,
        16,
        17,
        18,
        32,
        20,
        21,
        22,
        35,
        36,
        37,
        38,
        39,
        40,
        41,
        42,
        43,
        44,
        45,
        46,
        47,
        48,
        49,
        50,
        51,
        52,
        53,
        54,
        55,
        56,
        57,
        58,
        59,
        60,
        61,
        62,
        63,
        64,
        65,
        66,
        67,
        68,
        69,
        70,
        71,
        72,
        73,
        74,
        75,
        76,
        77,
        78,
        79,
        80,
        81,
        82,
        83,
        84,
        30,
        13,
        14
    };
}
