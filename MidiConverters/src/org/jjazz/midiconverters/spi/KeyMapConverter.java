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
package org.jjazz.midiconverters.spi;

import org.jjazz.midi.DrumKit;
import org.jjazz.midi.DrumKit;

/**
 * Map notes from a source DrumKit to a destination DrumKit.
 */
public interface KeyMapConverter
{
    String getConverterId();
  
    /**
     * Try to Map srcPitch from srcKit into the corresponding note in destKit.
     *
     * @param srcKit
     * @param srcPitch
     * @param destKit
     * @return -1 If srcPitch could not be mapped.
     */
    int convertKey(DrumKit srcKit, int srcPitch, DrumKit destKit);
}