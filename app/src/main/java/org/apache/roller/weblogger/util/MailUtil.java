/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */

package org.apache.roller.weblogger.util;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.util.MailUtilInstance.MailingException;

import javax.mail.MessagingException;
import java.util.List;


/**
 * A utility class for helping with sending email. 
 */
public class MailUtil {

    private static final MailUtilInstance INSTANCE = new MailUtilInstance();

    public static boolean isMailConfigured() {
        return INSTANCE.isMailConfigured();
    }

    public static void sendMessage(String from, String[] to, String[] cc, String[] bcc, String subject, String content, String mimeType) throws MessagingException {
        INSTANCE.sendMessage(from, to, cc, bcc, subject, content, mimeType);
    }

    public static void sendHTMLMessage(String from, String[] to, String[] cc, String[] bcc, String subject, String content) throws MessagingException {
        INSTANCE.sendHTMLMessage(from, to, cc, bcc, subject, content);
    }

    public static void sendEmailApprovalNotifications(List<WeblogEntryComment> comments, I18nMessages resources) throws MailingException {
        INSTANCE.sendEmailApprovalNotifications(comments, resources);
    }

    public static void sendPendingEntryNotice(WeblogEntry entry) throws WebloggerException {
        INSTANCE.sendPendingEntryNotice(entry);
    }

    public static void sendWeblogInvitation(Weblog website, User user) throws WebloggerException {
        INSTANCE.sendWeblogInvitation(website, user);
    }

    public static void sendTextMessage(String from, String[] to, String[] cc, String[] bcc, String subject, String content) throws MessagingException {
        INSTANCE.sendTextMessage(from, to, cc, bcc, subject, content);
    }

    public static void sendEmailNotification(WeblogEntryComment commentObject, RollerMessages messages, I18nMessages resources, boolean notifySubscribers) throws MailingException {
        INSTANCE.sendEmailNotification(commentObject, messages, resources, notifySubscribers);
    }

    public static void sendUserActivationEmail(User user) throws WebloggerException {
        INSTANCE.sendUserActivationEmail(user);
    }

    public static void sendEmailApprovalNotification(WeblogEntryComment cd, I18nMessages resources) throws MailingException {
        INSTANCE.sendEmailApprovalNotification(cd, resources);
    }
}
