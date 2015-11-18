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
package org.janelia.saalfeldlab.barrel;

import mpicbg.models.CoordinateTransform;

/**
 * Abstract base class for the two barrel distortions
 *
 * @author Stephan Saalfeld &lt;saalfelds@janelia.hhmi.org&gt;
 * @author Kie Ito &lt;itok@janelia.hhmi.org&gt;
 */
abstract public class AbstractBarrelDistortion2D implements CoordinateTransform {

    /* offset */
    protected double cx = 0;
    protected double cy = 0;

    /* radius normalization */
    protected double norm = 1.0;

    public AbstractBarrelDistortion2D(final double width, final double height) {
        setDimensions(width, height);
    }

    public void setDimensions(final double w, final double h) {
        cx = 0.5 * w;
        cy = 0.5 * h;
        norm = Math.sqrt(cx * cx + cy * cy);
    }

    @Override
    public double[] apply(final double[] p) {
        final double[] q = p.clone();
        applyInPlace(q);
        return q;
    }
}
