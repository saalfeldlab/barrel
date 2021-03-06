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

import org.janelia.saalfeldlab.barrel.BarrelDistortion2D;

import ij.gui.GenericDialog;

/**
 * Interactive plugin filter for simple 2D barrel distortion
 *
 * @author Stephan Saalfeld <saalfelds@janelia.hhmi.org>
 */
public class BarrelDistortion extends AbstractBarrelDistortion {

    static protected double k1 = 0.0;
    static protected double k2 = 0.0;

    @Override
    protected BarrelDistortion2D createTransform(final int w, final int h) {
        return new BarrelDistortion2D(w, h, k1, k2);
    }

    @Override
    protected String getTitle() {
        return "Barrel Distortion Correction";
    }

    @Override
    protected void addSpecialFields(final GenericDialog gd) {
        gd.addNumericField("k1 : ", k1 * 100.0, 2, 6, "%");
        gd.addNumericField("k2 : ", k2 * 100.0, 2, 6, "%");
    }

    @Override
    protected void readSpecialFields(final GenericDialog gd) {
        k1 = gd.getNextNumber() / 100.0;
        k2 = gd.getNextNumber() / 100.0;
    }
}
