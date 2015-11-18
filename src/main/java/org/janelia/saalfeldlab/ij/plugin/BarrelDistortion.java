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

import java.awt.AWTEvent;

import org.janelia.saalfeldlab.barrel.BarrelDistortion2D;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import mpicbg.ij.TransformMeshMapping;
import mpicbg.models.CoordinateTransformMesh;

/**
 * Interactive plugin filter for simple 2D barral distortion
 *
 * @author Stephan Saalfeld <saalfelds@janelia.hhmi.org>
 */
public class BarrelDistortion implements ExtendedPlugInFilter, DialogListener {

    static protected double k1 = 0.0;
    static protected double k2 = 0.0;
    static protected int meshResolution = 128;
    static protected int interpolationMethod = ImageProcessor.BILINEAR;
    static protected int interpolationMethodIndex = 1;
    final static String[] interpolationMethods = new String[]{"nearest neighbor", "bilinear", "bicubic"};
    final static protected int flags = DOES_8G | DOES_16 | DOES_32 | DOES_RGB | DOES_STACKS;


    @Override
    public void run(final ImageProcessor ip) {
        final BarrelDistortion2D t = new BarrelDistortion2D(ip.getWidth(), ip.getHeight(), k1, k2);
        final CoordinateTransformMesh mesh = new CoordinateTransformMesh(t, meshResolution, ip.getWidth(), ip.getHeight());
        final TransformMeshMapping<CoordinateTransformMesh> mapping = new TransformMeshMapping<CoordinateTransformMesh>(mesh);
        final ImageProcessor src = ip.duplicate();
        src.setInterpolationMethod(interpolationMethod);
        ip.set(0);
        mapping.mapInterpolated(src, ip);
    }

    @Override
    public int setup(final String arg0, final ImagePlus imp) {
        return flags;
    }

    @Override
    public void setNPasses(final int arg0) {}


    @Override
    public int showDialog(final ImagePlus imp, final String command, final PlugInFilterRunner pfr) {
        final GenericDialog gd = new GenericDialog("Barrel Distortion Correction");
        gd.addNumericField("k1 : ", k1 * 100.0, 2, 6, "%");
        gd.addNumericField("k2 : ", k2 * 100.0, 2, 6, "%");
        gd.addNumericField("mesh_resolution : ", meshResolution, 0 );
        gd.addChoice("interpolation_method :", interpolationMethods, interpolationMethods[interpolationMethodIndex]);
        gd.addPreviewCheckbox(pfr);
        gd.addDialogListener(this);

        gd.showDialog();
        if (gd.wasCanceled())
            return DONE;

        IJ.register(this.getClass());
        return IJ.setupDialog(imp, flags);
    }

    @Override
    public boolean dialogItemChanged(final GenericDialog gd, final AWTEvent e) {
        k1 = gd.getNextNumber() / 100.0;
        k2 = gd.getNextNumber() / 100.0;
        meshResolution = (int)gd.getNextNumber();
        interpolationMethodIndex = gd.getNextChoiceIndex();
        switch (interpolationMethodIndex) {
        case 0:
            interpolationMethod = ImageProcessor.NEAREST_NEIGHBOR;
            break;
        case 2:
            interpolationMethod = ImageProcessor.BICUBIC;
            break;
        default:
            interpolationMethod = ImageProcessor.BILINEAR;
        }

        return !gd.invalidNumber();
    }
}
