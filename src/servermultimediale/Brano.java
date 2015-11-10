/*
 * Copyright (C) 2015 betacentury
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package servermultimediale;

/**
 *
 * @author betacentury
 */
public class Brano {
    
    private final String path;
    
    public Brano(String _path)
    {
        path = _path;
    }
    @Override
    public String toString()
    {
        if ( path != null ) {
            String split[] = path.split("/");
            return split[split.length-1];
        }
        return null;
    }
    public String getPath()
    {
        return path;
    }
}
