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

/**
 * 2D barrel distortion
 *
 * @author Stephan Saalfeld &lt;saalfelds@janelia.hhmi.org&gt;
 * @author Kie Ito &lt;itok@janelia.hhmi.org&gt;
 */
public class BarrelDistortion2D extends AbstractBarrelDistortion2D {

    /* coefficients */
    protected double k1 = 0;
    protected double k2 = 0;

    public BarrelDistortion2D(final double width, final double height, final double k1, final double k2) {
        super(width, height);
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public void applyInPlace(final double[] p) {
        final double x = p[0] - cx;
        final double y = p[1] - cy;

        final double r = Math.sqrt(x * x + y * y) / norm;
        final double r2 = r * r;
        final double r4 = r2 * r2;
        final double ab = (1.0 + k1 * r2 + k2 * r4) / (1.0 + k1 + k2);

        p[0] = cx + x * ab;
        p[1] = cy + y * ab;
    }
}
