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

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import mpicbg.ij.TransformMeshMapping;
import mpicbg.models.CoordinateTransform;
import mpicbg.models.CoordinateTransformMesh;

/**
 * Interactive plugin filter for simple 2D barrel distortion
 *
 * @author Stephan Saalfeld <saalfelds@janelia.hhmi.org>
 */
abstract public class AbstractBarrelDistortion implements ExtendedPlugInFilter, DialogListener {

    static protected int meshResolution = 128;
    static protected int interpolationMethod = ImageProcessor.BILINEAR;
    static protected int interpolationMethodIndex = 1;
    final static String[] interpolationMethods = new String[]{"nearest neighbor", "bilinear", "bicubic"};
    final static protected int flags = DOES_8G | DOES_16 | DOES_32 | DOES_RGB | DOES_STACKS;

    abstract protected CoordinateTransform createTransform(final int w, final int h);
    abstract String getTitle();
    abstract void addSpecialFields(final GenericDialog gd);
    abstract void readSpecialFields(final GenericDialog gd);

    @Override
    public void run(final ImageProcessor ip) {
        final CoordinateTransform t = createTransform(ip.getWidth(), ip.getHeight());
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
        final GenericDialog gd = new GenericDialog(getTitle());
        addSpecialFields(gd);
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
        readSpecialFields(gd);
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
