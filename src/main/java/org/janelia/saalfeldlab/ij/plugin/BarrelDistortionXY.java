/**
 * License: GPL
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.janelia.saalfeldlab.ij.plugin;

import org.janelia.saalfeldlab.barrel.BarrelDistortion2DXY;

import ij.gui.GenericDialog;

/**
 * Interactive plugin filter for 2D barrel distortion with independent coefficients for x and y
 *
 * @author Stephan Saalfeld <saalfelds@janelia.hhmi.org>
 */
public class BarrelDistortionXY extends AbstractBarrelDistortion {

    static protected double k1x = 0.0;
    static protected double k2x = 0.0;
    static protected double k1y = 0.0;
    static protected double k2y = 0.0;

    @Override
    protected BarrelDistortion2DXY createTransform(final int w, final int h) {
        return new BarrelDistortion2DXY(w, h, k1x, k2x, k1y, k2y);
    }

    @Override
    protected String getTitle() {
        return "XY Barrel Distortion Correction";
    }

    @Override
    protected void addSpecialFields(final GenericDialog gd) {
        gd.addNumericField("k1_horizontal : ", k1x * 100.0, 2, 6, "%");
        gd.addNumericField("k2_horizontal : ", k2x * 100.0, 2, 6, "%");
        gd.addNumericField("k1_vertical : ", k1y * 100.0, 2, 6, "%");
        gd.addNumericField("k2_vertical : ", k2y * 100.0, 2, 6, "%");
    }

    @Override
    protected void readSpecialFields(final GenericDialog gd) {
        k1x = gd.getNextNumber() / 100.0;
        k2x = gd.getNextNumber() / 100.0;
        k1y = gd.getNextNumber() / 100.0;
        k2y = gd.getNextNumber() / 100.0;
    }
}
