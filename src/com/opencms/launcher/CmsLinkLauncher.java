/*
* File   : $Source: /alkacon/cvs/opencms/src/com/opencms/launcher/Attic/CmsLinkLauncher.java,v $
* Date   : $Date: 2003/01/20 23:59:23 $
* Version: $Revision: 1.16 $
*
* This library is part of OpenCms -
* the Open Source Content Mananagement System
*
* Copyright (C) 2001  The OpenCms Group
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* For further information about OpenCms, please see the
* OpenCms Website: http://www.opencms.org
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.opencms.launcher;

import com.opencms.core.A_OpenCms;
import com.opencms.core.CmsException;
import com.opencms.core.I_CmsConstants;
import com.opencms.file.CmsFile;
import com.opencms.file.CmsObject;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * OpenCms launcher class for starting template classes implementing
 * the I_CmsDumpTemplate interface.
 * This can be used for plain text files or files containing graphics.
 * <P>
 * If no other start template class is given, CmsDumpTemplate will
 * be used to create output.
 *
 * @author Michael Emmerich
 * @version $Revision: 1.16 $ $Date: 2003/01/20 23:59:23 $
 */
public class CmsLinkLauncher extends A_CmsLauncher {

    /**
     * The currently running OpenCms instance.
     */
    private A_OpenCms m_openCms;

    /**
     * The html-code for returning the export file for external links
     */
    private static String C_EXPORTED_LINKHTML_01 = "<html><head><meta http-equiv="+'"'+"refresh"+'"'+" content="+'"'+"0; url=";
    private static String C_EXPORTED_LINKHTML_02 = '"'+"></head><body></body></html>";

    /**
     * Gets the ID that indicates the type of the launcher.
     * @return launcher ID
     */
    public int getLauncherId() {
        return C_TYPE_LINK;
    }

    /**
     * Unitary method to start generating the output.
     * Every launcher has to implement this method.
     * In it possibly the selected file will be analyzed, and the
     * Canonical Root will be called with the appropriate
     * template class, template file and parameters. At least the
     * canonical root's output must be written to the HttpServletResponse.
     *
     * @param cms CmsObject Object for accessing system resources
     * @param file CmsFile Object with the selected resource to be shown
     * @param startTemplateClass Name of the template class to start with.
     * @param openCms a instance of A_OpenCms for redirect-needs
     * @throws CmsException
     */
    protected void launch(CmsObject cms, CmsFile file,
                          String startTemplateClass,
                          A_OpenCms openCms) throws CmsException {

        // get the current running OpenCms instance.
        if(openCms == null) {
            openCms = m_openCms;
        }
        String link = new String(file.getContents());
        if(link == null || "".equals(link) || " ".equals(link)){
            throw new CmsException(CmsException.C_NOT_FOUND);
        }
        if( link.startsWith("/") ) {
            // internal link ...
            CmsFile linkFile = cms.readFile(link);
            openCms.showResource(cms, linkFile);
        } else {
            // redirect to external link ...
            if (cms.getMode() != I_CmsConstants.C_MODUS_EXPORT){
                HttpServletResponse response =
                    (HttpServletResponse)
                        cms.getRequestContext().getResponse().getOriginalResponse();
                try {
                    response.sendRedirect(link);
                } catch (IOException e) {
                }
            } else {
                try{
                    OutputStream responsestream = cms.getRequestContext().getResponse().getOutputStream();
                    StringBuffer htmlStream = new StringBuffer(C_EXPORTED_LINKHTML_01);
                    htmlStream.append(link);
                    htmlStream.append(C_EXPORTED_LINKHTML_02);
                    responsestream.write(htmlStream.toString().getBytes());
                    responsestream.close();
                } catch (IOException e){
                }
            }
        }
    }

    /**
     * Sets the currently running OpenCms instance.
     */
    public void setOpenCms(A_OpenCms openCms) {
        m_openCms = openCms;
    }
}
