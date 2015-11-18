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
 * 2D barrel distortion with independent coefficients for x and y
 *
 * @author Stephan Saalfeld &lt;saalfelds@janelia.hhmi.org&gt;
 * @author Kie Ito &lt;itok@janelia.hhmi.org&gt;
 */
public class BarrelDistortion2DXY extends AbstractBarrelDistortion2D {

    /* coefficients */
    protected double k1x = 0;
    protected double k2x = 0;
    protected double k1y = 0;
    protected double k2y = 0;

    public BarrelDistortion2DXY(
            final double width,
            final double height,
            final double k1x,
            final double k2x,
            final double k1y,
            final double k2y) {
        super(width, height);
        this.k1x = k1x;
        this.k2x = k2x;
        this.k1y = k1y;
        this.k2y = k2y;
    }

    @Override
    public void applyInPlace(final double[] p) {
        final double x = p[0] - cx;
        final double y = p[1] - cy;

        final double r = Math.sqrt(x * x + y * y) / norm;
        final double r2 = r * r;
        final double r4 = r2 * r2;
        final double abx = (1.0 + k1x * r2 + k2x * r4) / (1.0 + k1x + k2x);
        final double aby = (1.0 + k1y * r2 + k2y * r4) / (1.0 + k1y + k2y);

        p[0] = cx + x * abx;
        p[1] = cy + y * aby;
    }
}
