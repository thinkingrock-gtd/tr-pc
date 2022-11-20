/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package tr.model.util.delegation;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.util.NbBundle;
import tr.model.action.Action;
import tr.model.thought.Thought;
import tr.model.util.DataServices;
import tr.model.util.DataServices.Predicate;
import static tr.model.util.delegation.DelegationUtils.getResponseDelegateData;

/**
 * Class to check if a delegation response is appropriate and to email the
 * response.
 *
 * @author Jeremy Moore
 */
class DelegationResponder {

    private final static Logger LOG = Logger.getLogger("tr.model.util.delegation"); 

   
    static synchronized void process(Action action) {
        if (action == null) {
            return;
        }
        Thread thread = new Thread("Delegation response") {
            @Override
            public void run() {
                new DelegationResponder(action).process();
            }
        };
        thread.start();
    }


    private final Action delegationAction;
    private final Thought delegationThought;
    private ProgressHandle progressHandle;
   
    /**
     * Create a new instance for a delegation action.
     * @param delegationAction The delegation action.
     */
    private DelegationResponder(Action delegationAction) {
        this.delegationAction = delegationAction;
        this.delegationThought = delegationAction.getThought();
    }

    public void process() {
        if (isLastDelegatedAction()) {
            sendResponse();
        }
    }
    
    /**
     * See if there are any other actions for the delegation that are not done.
     *
     * @return true if there are no such actions.
     */
    private boolean isLastDelegatedAction() {

        Predicate<Action> actionForDelegationNotDone = (Action action) -> {
            Thought thought = action.getThought();
            return thought != null
                && thought.getID() == delegationThought.getID()
                && action.getID() != delegationAction.getID()
                && !action.isDone();
        };

        DataServices ds = new DataServices();

        return !ds.exists(actionForDelegationNotDone);
    }

    
    private void sendResponse() {
        String reply = delegationThought.getDelegationReply();
        if (StringUtils.isBlank(reply)) {
            LOG.info("No reply email address.");
            return;
        }
        SMTP smtp = EmailPrefs.getSMTP();
        if (smtp == null) {
            LOG.info("No valid Email SMTP preferences (settings).");
            return;
        }
        if (!smtp.valid()) {
            LOG.info("No valid Email SMTP preferences (settings).");
            return;
        }
        String from = EmailPrefs.getEmailAddress();
        if (StringUtils.isBlank(from)) {
            LOG.info("No valid Email preferences (settings).");
            return;
        }
        
        String dd = getResponseDelegateData(delegationAction).serialize();
        
        try {
            progressStart(5);
            progress(1);
            progress(2);
            progress(3);            
            SimpleEmail email = new SimpleEmail();
            email.setHostName(smtp.host);
            email.setSmtpPort(smtp.port);
            email.setAuthenticator(new DefaultAuthenticator(smtp.user, smtp.pass));
            email.setSSLOnConnect(smtp.ssl);
            email.setFrom(from);
            email.setSubject(delegationAction.getDescription());
            email.setMsg(delegationAction.getNotes() + "\n\n" + dd);
            email.addTo(reply);
            email.send();
            progress(4);
            progress(5);
            progressFinish();            
            LOG.log(Level.INFO, "Emailed delegation response.");
        } catch (EmailException e) {
            LOG.log(Level.SEVERE, "Failed to send email. Check Email SMTP settings.");
        }
    }    
    
    private void progressStart(int units) {
        progressHandle = ProgressHandle.createHandle(NbBundle.getMessage(SMTPSender.class, "sending.email"));        
        progressHandle.start(units);
        sleep(400);        
    }
    
    private void progress(int units) {
        progressHandle.progress(units);
        sleep(400);        
    }
    
    private void progressFinish() {
        progressHandle.finish();
        sleep(500);
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }
    
    
    
}
