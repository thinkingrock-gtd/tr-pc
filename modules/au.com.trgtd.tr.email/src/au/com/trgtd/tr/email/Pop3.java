/*
 * @(#)Pop3.java
 *
 * Copyright (C) Franck Andriano
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * See GPL license : http://www.gnu.org/licenses/gpl.html
 *
 */
package au.com.trgtd.tr.email;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.Flags.Flag;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

/**
 * Classe permettant de rÈcupÈrer des mails sur un serveur Pop3 ou Imap4 (avec SSL ou non) <br>
 * les fichiers en attachement sont dÈposÈs sur un disk! <br>
 *   <br>
 *  http://java.sun.com/products/javamail/  <br>
 *  - mail.jar 1.4 (pop3.jar, smtp.jar, imap.jar, mailapi.jar, dsn.jar) <br>
 *  - activation.jar  <br>
 *   <br>
 *  Port 110 --> POP3<br>
 *  Port 995 --> POP3-SSL<br>
 *  Port 143 --> IMAP<br>
 *  Port 993 --> IMAP-SSL <br>
 *   <br>
 *  Defaut Folder : INBOX, Drafts, Sent, Trash <br>
 *   <br><br>
 * <br>
 * SSL explication et exemple : <br>
 *  <br>
 * Url : https://altern.org/ <br>
 * Exporter le certificat du site web dans fichier ex : altern.cer  <br>
 *  <br> <br>
 * Importer le certificat dans le magasin cacerts, \j2sdk1.4.x_xx\jre\lib\security\ <br>
 * $> keytool -import -keystore cacerts -file altern.cer <br>
 *  <br> <br>
 * Le mot de passe du magasin global est par dÈfaut : 'changeit' <br>
 *
 ***
 *
 * @author  Franck Andriano, nexus6@altern.org
 * @version 2.0 2007
 */
public class Pop3 {

    /**
     * Dossier des fichiers en attachement par default!
     */
    public static String c_dir = "/tmp/";
    /**
     *  file separator system...
     */
    public static String a_sep = System.getProperty("file.separator");
    /**
     *  line separator system...
     */
    public static String a_line = System.getProperty("line.separator");
    /**
     * Nom du serveur
     */
    private String c_host = "";
    /**
     * Nom de l'utilisateur.
     */
    private String c_username = "";
    /**
     * Mot de passe de l'utilisateur.
     */
    private String c_password = "";
    /**
     * Objet session....
     */
    private Session session = null;
    /**
     * Objet store liÈ ‡ une session...
     */
    private Store store = null;
    /**
     * Objet folder liÈ ‡ un store, (dossier INBOX par defaut)
     */
    private Folder folder = null;
    /**
     * Array Folder liÈ ‡ un autre folder (liste sous dossier IMAP abonnÈ ou non)
     */
    private Folder folders[] = null;
    /**
     * Array Folder, dossiers racines IMAP (liste dossiers racines IMAP abonnÈ ou non)
     */
    private Folder defaut_folders[] = null;
    /**
     * Objet message liÈ ‡ un folder...
     */
    private Message message[] = null;
    /**
     * Port du serveur de mail par defaut
     */
    private int c_port = 110;
    /**
     * Protolcol du serveur de mail par defaut POP3.
     */
    private String c_protocol = "pop3";
    /**
     * POP3 protocole et port par defaut!
     */
    public final static String POP = "pop3";
    public final static int POP_PORT = 110;
    public final static int POPS_PORT = 995;
    /**
     * IMAP4 protocole et port par defaut!
     */
    public final static String IMAP = "imap";
    public final static int IMAP_PORT = 143;
    public final static int IMAPS_PORT = 993;
    /**
     * Connexion en POP3 SSL ou IMAP4 SSL ?
     */
    private boolean SSL = false;
    /**
     * TLS, STARTTLS command first
     */
    private boolean TLS = false;
    /**
     * Timeout, Socket control & I/O Socket
     */
    private int c_timeout = 0;
    /**
     * SÈparateur de dossier IMAP4
     */
    private char c_separator = '.';
    /**
     * Debug mode
     */
    private boolean c_debug = false;

    /**
     * Constructeur, ne fait rien si aucune variable de classe n'est paramÈtrÈe!
     */
    public Pop3() {
    }

    /**
     * Constructeur simple! (POP3 & port 110 par defaut)
     *
     * @param _dir            Dossier des fichiers en attachement
     * @param _host             Nom du serveur pop3
     * @param _username       Nom de l'utilisateur
     * @param _password       Mot de passe du compte
     */
    public Pop3(String _dir, String _host, String _username, String _password) {
        this(_dir, _host, _username, _password, 110, POP);
    }

    /**
     * Constructeur complet!
     *
     * @param _dir            Dossier des fichiers en attachement
     * @param _host             Nom du serveur pop3
     * @param _username           Nom de l'utilisateur
     * @param _password           Mot de passe du compte
     */
    public Pop3(String _dir, String _host, String _username, String _password, int _port, String _protocol) {
        Pop3.c_dir = _dir;
        this.c_host = _host;
        this.c_username = _username;
        this.c_password = _password;
        this.c_port = _port;
        this.c_protocol = _protocol;

        // vÈrif du sÈparateur systËme!
        if (!c_dir.endsWith(a_sep)) {
            c_dir += a_sep;
        }
    }

    /**
     * MÈthode setDir, dÈfini le dossier de destination des mails
     */
    public void setDir(String _dir) {
        Pop3.c_dir = _dir;
        //    vÈrif du sÈparateur systËme!
        if (!c_dir.endsWith(a_sep)) {
            c_dir += a_sep;
        }
    }

    /**
     * MÈthode setHost, dÈfini le server pop3
     */
    public void setHost(String _host) {
        this.c_host = _host;
    }

    /**
     * MÈthode setLogin, dÈfini le login du compte pop3
     */
    public void setLogin(String _username) {
        this.c_username = _username;
    }

    /**
     * MÈthode setPwd, dÈfini le pwd du compte pop3
     */
    public void setPwd(String _password) {
        this.c_password = _password;
    }

    /**
     * MÈthode setPort, dÈfini le port du server pop3
     */
    public void setPort(int _port) {
        this.c_port = _port;
    }

    /**
     * Determine si TLS, STARTTLS command first
     *
     * @param _ttls :  si true active TLS
     */
    public void setTLS(boolean _tls) {
        TLS = _tls;
    }

    /**
     * MÈthode getMail, rÈcupËre des mails simples et multiparts !
     *
     * @throws NoSuchProviderException, MessagingException Si une erreur survient.
     */
    public Message[] getMail() throws NoSuchProviderException, MessagingException {
        return getMail("INBOX", c_debug);
    }

    /**
     * MÈthode getMail, rÈcupËre des mails simples et multiparts !
     *
     * @param  boolean      mode debug true ou false
     * @exception NoSuchProviderException, MessagingException Si une erreur survient.
     */
    public Message[] getMail(boolean sDebug) throws NoSuchProviderException, MessagingException {
        return getMail("INBOX", sDebug);
    }

    /**
     * MÈthode getMail, rÈcupËre des mails simples et multiparts !
     *
     * @param  String     box INBOX, TRASH...
     * @throws NoSuchProviderException, MessagingException Si une erreur survient.
     */
    public Message[] getMail(String box) throws NoSuchProviderException, MessagingException {
        return getMail(box, false);
    }

    /**
     * MÈthode getMail, rÈcupËre des mails simples et multiparts !
     *
     * @param  String     box INBOX, TRASH...
     * @param  boolean      mode debug true ou false
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public Message[] getMail(String box, boolean sDebug) throws NoSuchProviderException, MessagingException {
        // check box
        if (box == null) {
            throw new MessagingException("Folder is null, defaut is INBOX!");
        }

        // Get properties
        Properties props = getSessionProperties();

        // Get session
        session = Session.getDefaultInstance(props, null);

        // option debug
        session.setDebug(sDebug);

        if (SSL) {
            // Get SSL the store
            URLName url = new URLName(c_protocol, c_host, c_port, null, c_username, c_password);

            if (c_protocol.equals(POP)) {
                store = new POP3SSLStore(session, url);
            } else if (c_protocol.equals(IMAP)) {
                store = new IMAPSSLStore(session, url);
            }

            // connection!
            store.connect();
        } else {
            // Get the store
            store = session.getStore(c_protocol);

            // connection!
            store.connect(c_host, c_username, c_password);
        }

        // on est connectÈ ?
        if (store.isConnected()) {
            // liste tous les dossiers racine (ROOT abonnÈ ou non...)
            Folder defaultFolder = store.getDefaultFolder();
            if (defaultFolder == null) {
                throw new MessagingException("Can't find defaut FOLDER !!!");
            }

            // rÈcupËre la liste des dossiers racines
            defaut_folders = defaultFolder.list();

            // rÈcupËre le sÈparateur de dossiers du serveur
            this.c_separator = defaultFolder.getSeparator();

            // rÈcupËre le dossier
            folder = store.getFolder(box);

            // check pour box="%"!
            if (folder.exists()) {
                // on peut effacer les mails
                folder.open(Folder.READ_WRITE);

                // rÈcupËre les messages du dossier courant
                if (folder.isOpen()) {
                    // rÈcupËre la liste des dossiers, imap uniquement
                    try {
                        folders = folder.list();
                    } catch (MessagingException me) {
                    }

                    message = folder.getMessages();
                }
            }
        }

        // retourne un Array Message tout en gardant la session ouverte...
        return message;
    }

    /**
     * ParamËtrage de la session
     *
     * @return Properties
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    private Properties getSessionProperties() throws MessagingException {
        // Get system properties
        Properties props = System.getProperties();

        // only for Microsoft Exchange...
        //props.put("mail.pop3.forgettopheaders", "true");

        // Setup mail server port
        if (c_protocol.equals(POP)) {
            props.put("mail.pop3.port", "" + c_port);
            if (TLS) {
                props.put("mail.pop3.starttls.enable", "true");
            }
            if (c_timeout != 0) {
                props.put("mail.pop3.connectiontimeout", "" + c_timeout);
                props.put("mail.pop3.timeout", "" + c_timeout);
            }
        } else if (c_protocol.equals(IMAP)) {
            props.put("mail.imap.port", "" + c_port);

            if (TLS) {
                props.put("mail.imap.starttls.enable", "true");
            }
            if (c_timeout != 0) {
                props.put("mail.imap.connectiontimeout", "" + c_timeout);
                props.put("mail.imap.timeout", "" + c_timeout);
            }
        } else {
            throw new MessagingException("Unknow Protocol : " + c_protocol);
        }

        // mode debug
        if (c_debug) {
            props.put("mail.debug", "true");
        }

        //props.put("mail.transport.protocol", c_protocol);

        if (SSL) {
            // add new provider
            try {
                    Security.addProvider(Security.getProvider("SunJSSE"));
            } catch (SecurityException se) {
                throw new MessagingException("" + se);
            }

            if (c_protocol.equals(POP)) {
                props.put("mail.pop3.socketFactory.port", "" + c_port);
                props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.pop3.socketFactory.fallback", "true");
            } else if (c_protocol.equals(IMAP)) {
                //props.put("mail.imap.auth.login.disable", "false");
                //props.put("mail.imap.auth.plain.disable", "true");
                //props.put("mail.imap.fetchsize", "" + 16384);
                props.put("mail.imap.socketFactory.port", "" + c_port);
                props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.fallback", "true");
            }
        }
        return props;
    }

    /**
     * DÈfini l'option de dÈbuggage
     *
     * @param _debug   BoolÈan de l'option de dÈbuggage
     */
    public void setDebug(boolean _debug) {
        c_debug = _debug;
    }

    /**
     * Indique le timeout sur la socket.
     * @param timeout Timeout en milliseconds.
     */
    public void setSocketTimeout(int timeout) {
        this.c_timeout = timeout;
    }

    /**
     * MÈthode close qui ferme une session pop3
     *
     * @param _del                  On efface les messages ?
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public void close(boolean _del) throws MessagingException {
        if (folder.isOpen()) {
            folder.close(_del); // on efface les messages ou pas!
        }
        store.close();
    }

    /**
     * Determine si la connexion au un serveur POP3 ou IMAP4 est sÈcurisÈe par SSL.
     *
     * @param _ssl : si true active une connexion SSL
     */
    public void setSecurePOP3(boolean _ssl) {
        SSL = _ssl;
    }

    /**
     * MÈthode isNew message
     *
     * @param num NumÈro du message
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public boolean isNew(int num) throws MessagingException {
        return message[num].isSet(Flag.RECENT);
    }

    /**
     * MÈthode isRead, ce message a ÈtÈ lu ?
     *
     * @param num NumÈro du message
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public boolean isRead(int num) throws MessagingException {
        return message[num].isSet(Flag.SEEN);
    }

    /**
     * MÈthode countMessage, compte tous les messages dans la box
     *
     * @param num NumÈro du message
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public int countMessage() throws MessagingException {
        return folder.getMessageCount();
    }

    /**
     * Retourne le nombre de message non lu
     *
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public int countMessageUnread() throws MessagingException {
        return folder.getUnreadMessageCount();
    }

    /**
     * Retourne le nombre de nouveau message
     *
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public int countNewMessage() throws MessagingException {
        return folder.getNewMessageCount();
    }

    /**
     * Retourne le nombre de message lu
     *
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public int countMessageRead() throws MessagingException {
        return (countMessage() - countMessageUnread());
    }

    /**
     * Retourne le nombre d'ancien message
     *
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public int countOldMessage() throws MessagingException {
        return (countMessage() - countNewMessage());
    }

    /**
     * Delete Message
     *
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public void deleteMessage(int num) throws MessagingException {
        message[num].setFlag(Flags.Flag.DELETED, true);
    }

    /**
     * CrÈe un nouveau dossier IMAP de le dossier courant...
     *
     * @param _folder
     * @return bollean true si tout s'est bien passÈ
     * @throws MessagingException MessagingException Si une erreur survient.
     */
    public boolean createFolder(String _folder) throws MessagingException {
        if (_folder == null) {
            return false;
        }
        Folder newFolder = this.store.getFolder(_folder);
        return newFolder.create(Folder.HOLDS_MESSAGES); //1 - for HOLD_MESSAGES
    }

    /**
     * DÈtruit un Folder IMAP (ATTENTION permanent!)
     *
     * @param _folder
     * @return bollean true si tout s'est bien passÈ
     * @throws MessagingException
     */
    public boolean deleteFolder(String _folder) throws MessagingException {
        // folderPrefix + userFolderPrefix + folderName
        if (_folder == null) {
            return false;
        }
        Folder del_folder = this.store.getFolder(_folder);
        if (del_folder.exists()) {
            if (del_folder.isOpen()) {
                del_folder.close(true);
            }
            return del_folder.delete(false);
        }
        return false;
    }

    /**
     * Copie tous les messages d'un dossier IMAP vers un autre...
     *
     * @param _srcFolder
     * @param _destFolder
     * @return bollean true si tout s'est bien passÈ
     * @throws MessagingException
     */
    public boolean copyFolderMessages(String _srcFolder, String _destFolder) throws MessagingException {
        if (_srcFolder == null || _destFolder == null) {
            return false;
        }
        Folder sourceF = this.store.getFolder(_srcFolder);
        Folder targetF = this.store.getFolder(_destFolder);
        if (sourceF.exists() && targetF.exists()) {
            sourceF.open(Folder.READ_ONLY);
            targetF.open(Folder.READ_WRITE);
            if (sourceF.isOpen() && targetF.isOpen()) {
                Message[] msgs = sourceF.getMessages();
                sourceF.copyMessages(msgs, targetF);
                return true;
            }
        }
        return false;
    }

    /**
     * Renomme un dossier IMAP, copie des messages avant destruction de la source (ATTENTION permanent!)
     *
     * @param _srcFolder Dossier ‡ renomer
     * @param _destFolder Dissier cible
     * @return bollean true si tout s'est bien passÈ
     * @throws MessagingException
     */
    public boolean renameFolder(String _srcFolder, String _destFolder) throws MessagingException {
        if (_srcFolder == null || _destFolder == null) {
            return false;
        }
        Folder targetF = null;
        Folder sourceF = store.getFolder(_srcFolder);
        sourceF.open(Folder.READ_ONLY);
        if (sourceF.exists() && sourceF.isOpen() && createFolder(_destFolder)) {
            targetF = this.store.getFolder(_destFolder);
            targetF.open(Folder.READ_WRITE);
            if (targetF.exists() && targetF.isOpen()) {
                Message[] msgs = sourceF.getMessages();
                sourceF.copyMessages(msgs, targetF);

                // finallement on tente de dÈtruit la source...
                if (sourceF.isOpen()) {
                    sourceF.close(true);
                }
                return sourceF.delete(true);
            }
        }
        return false;
    }

    /**
     * S'abonner ou se dÈsabonner ‡ un dossier IMAP (Subscrib / UnSubcrib Folder IMAP)
     *
     * @param _folder
     * @return
     * @throws MessagingException
     */
    public boolean setSubscribed(String _folder, boolean _subscribed) throws MessagingException {
        if (_folder == null) {
            return false;
        }
        Folder subcr_folder = this.store.getFolder(_folder);
        if (!subcr_folder.exists()) {
            return false;
        }
        subcr_folder.setSubscribed(_subscribed);
        return true;
    }

    /**
     * Retourne tous les dossiers abonnÈs courants
     *
     * @return Folder[]
     */
    public Folder[] getSubscribedFolder() {
        if (folders == null) {
            return null;
        }
        ArrayList<Folder> array = new ArrayList<>(); //auto size!
        for (int i = 0, n = folders.length; i < n; i++) {
            if (this.folders[i].isSubscribed()) {
                array.add(this.folders[i]);
            }
        }
        Folder[] subcr_folder = new Folder[array.size()];
        array.toArray(subcr_folder);
        return subcr_folder;
    }

    /**
     * Retourne tous les dossiers non abonnÈs courants
     *
     * @return Folder[]
     */
    public Folder[] getUnSubscribedFolder() {
        if (folders == null) {
            return null;
        }
        ArrayList<Folder> array = new ArrayList<>(); //auto size!
        for (int i = 0, n = folders.length; i < n; i++) {
            if (!this.folders[i].isSubscribed()) {
                array.add(this.folders[i]);
            }
        }
        Folder[] subcr_folder = new Folder[array.size()];
        array.toArray(subcr_folder);
        return subcr_folder;
    }

    /**
     * Retourne l'objet Folder[] courant
     *
     * @return char  separateur de dossier
     */
    public char getSep() {
        return this.c_separator;
    }

    /**
     * Retourne l'objet Folder courant
     *
     * @return Folder
     */
    public Folder getFolder() {
        return this.folder;
    }

    /**
     * Retourne l'objet Folder[] courant
     *
     * @return Folder[]
     */
    public Folder[] getFolders() {
        return this.folders;
    }

    /**
     * Retourne l'objet Folder[] dossier racine IMAP
     *
     * @return Folder[]
     */
    public Folder[] getRootFolders() {
        return this.defaut_folders;
    }

    /**
     * Class interne gÈrant les types de fichier attachÈ ‡ un mail
     */
    public final static class Tools {

        /**
         * MÈthode saveFile, sauve un fichier sur le disk!
         *
         * @param filename Chemin complet du fichier ! (sinon rÈpertoire courant)
         * @param input Flux du fichier...
         * @exception IOException Si une erreur d'Ècriture survient.
         */
        public static String saveFile(String _filename, InputStream _input) throws IOException {
            if (_filename == null) {
                _filename = File.createTempFile("xx", ".out").getName();
            }

            File file = new File(c_dir + _filename);
            FileOutputStream fos = new FileOutputStream(file);

            BufferedOutputStream bos = new BufferedOutputStream(fos);
            BufferedInputStream bis = new BufferedInputStream(_input);
            int aByte;
            while ((aByte = bis.read()) != -1) {
                bos.write(aByte);
            }

            bos.flush();
            bos.close();
            bis.close();

            return (c_dir + _filename);
        }

        /**
         * MÈthode getFile, rÈcupËre les fichiers attachÈ au message
         *
         * @param pop_message     Objet Message passÈ en paramËtre
         * @exception Exception       Exception Si une erreur survient.
         *
         * @return Vector        Retourne un Vector contenant les chemin complet des fichiers
         */
        public static Vector getFile(Message pop_message) throws Exception {
            Vector vec = new Vector();
            Object content = pop_message.getContent();
            if (content instanceof Multipart) {
                vec = getFileHandleMultipart((Multipart) content);
            }
            return vec;
        }

        /**
         * MÈthode qui le corps d'un message multipart...
         *
         * @param multipart             Objet multipart
         * @exception                 MessagingException, IOException Si une erreur survient.
         *
         * @return Vector        Retourne un Vector contenant les chemin complet des fichiers
         */
        public static Vector<String> getFileHandleMultipart(Multipart multipart) throws MessagingException, IOException {
            Vector<String> vec = new Vector<>();

            // boucle sur les parties d'un message...
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = (Part) multipart.getBodyPart(i);
                String disposition = part.getDisposition();
                if ((disposition != null) && (disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE))) {
                    String filename = MimeUtility.decodeText(part.getFileName()); // pour Lotus Notes...
                    String path_filename = saveFile(filename, part.getInputStream());
                    vec.add(path_filename);
                }
            }
            return vec;
        }

        /**
         * MÈthode getFile, rÈcupËre les fichiers attachÈs au message avec un ContentType dÈfinie
         *
         * @param pop_message         Objet Message passÈ en paramËtre
         * @param contentType            String contenant un Content Type
         * @exception Exception       Exception Si une erreur survient.
         *
         * @return Vector                Retourne un Vector contenant les chemin complet des fichiers
         */
        public static Vector getFileEmbed(Message pop_message, String _contentType) throws Exception {
            Vector vec = new Vector();
            Object content = pop_message.getContent();
            if (content instanceof Multipart) {
                vec = getFileHandleMultipart((Multipart) content, _contentType);
            }
            return vec;
        }

        /**
         * MÈthode qui rÈcupËre les fichiers attachÈs au message avec un ContentType dÈfinie
         * (exemple : images 'image/jpeg' contenu dans un mail html avec images embarquÈes...)
         *
         * @param multipart             Objet multipart
         * @param contentType            String contenant un Content Type
         * @exception                 MessagingException, IOException Si une erreur survient.
         *
         * @return Vector                Retourne un Vector contenant les chemin complet des fichiers
         */
        public static Vector<String> getFileHandleMultipart(Multipart multipart, String _contentType) throws MessagingException, IOException {
            Vector<String> vec = new Vector<>();

            // boucle sur les parties d'un message...
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = (Part) multipart.getBodyPart(i);
                String disposition = part.getDisposition();
                String ctype = part.getContentType();
                ContentType xctype = new ContentType(ctype.toLowerCase());

                if (disposition == null && xctype.match(_contentType)) {
                    if (part.getFileName() == null) {
                        continue;
                    }
                    String filename = MimeUtility.decodeText(part.getFileName()); // pour Lotus Notes...
                    String path_filename = saveFile(filename, part.getInputStream());
                    vec.add(path_filename);
                }
            }
            return vec;
        }

        /**
         * MÈthode getBody, rÈcupËre le corps du mail sans les headers
         *
         * @param pop_message     Objet Message passÈ en paramËtre
         * @exception Exception       Exception Si une erreur survient.
         *
         * @return String        Retourne un String contenant le corps (texte) du mail
         */
        public static String getBody(Message pop_message) throws Exception {
            String body = null;
            Object content = pop_message.getContent();
            if (content instanceof Multipart) {
                body = getBodyHandleMultipart((Multipart) content);
            } else {
                body = getBodyHandlePart(pop_message);
            }
            return body;
        }

        /**
         * MÈthode qui rÈcupËre le corps d'un message multipart...
         *
         * @param multipart          Objet multipart
         * @exception          MessagingException, IOException Si une erreur survient.
         * @return String          Retourne un String contenant le corps (texte ASCII) du mail
         */
        public static String getBodyHandlePart(Part part) throws MessagingException, IOException {
            String body = null;

            String ctype = part.getContentType();
            ContentType xctype = new ContentType(ctype.toLowerCase());
            StringBuilder sb = new StringBuilder();

            if (xctype.match("text/plain") || xctype.match("TEXT/PLAIN")) {
                BufferedReader xreader = getTextReader(part);
                for (String xline; (xline = xreader.readLine()) != null;) {
                    sb.append(xline).append('\n');
                }
                body = sb.toString();
            } else {



                throw new MessagingException("No text/plain in the message!");
            }

            return body;
        }

        /**
         * MÈthode qui rÈcupËre le corps d'un message multipart...
         *
         * @param multipart          Objet multipart
         * @exception                   MessagingException, IOException Si une erreur survient.
         * @return String          Retourne un String contenant le corps (texte ASCII) du mail
         */
        public static String getBodyHandleMultipart(Multipart multipart) throws MessagingException, IOException {
            String body = null;
            StringBuilder sb = new StringBuilder();

            // boucle sur les parties d'un message...
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = (Part) multipart.getBodyPart(i);
                Object content = part.getContent();
                if (content instanceof Multipart) // for multipart/mixed
                {
                    body = getBodyHandleMultipart((Multipart) content);
                    break;
                }

                String disposition = part.getDisposition();
                String ctype = part.getContentType();
                ContentType xctype = new ContentType(ctype.toLowerCase());

                if ((xctype.match("text/plain") || xctype.match("TEXT/PLAIN")) && disposition == null) {
                    BufferedReader xreader = getTextReader(part);
                    for (String xline; (xline = xreader.readLine()) != null;) {
                        sb.append(xline).append('\n');
                    }
                    body = sb.toString();
                }
            }

            if (body == null) {
                throw new MessagingException("No text/plain in the message!");
            }
            return body;
        }

        /**
         * MÈthode getBodyHtml, rÈcupËre le corps du mail en html
         *
         * @param pop_message     Objet Message passÈ en paramËtre
         * @exception Exception      Exception Si une erreur survient.
         *
         * @return String        Retourne un String contenant le corps (texte) du mail
         */
        public static String getBodyHtml(Message pop_message) throws Exception {
            String body = null;
            Object content = pop_message.getContent();
            if (content instanceof Multipart) {
                body = getBodyHandleMultipart((Multipart) content, "text/html");
            }
            return body;
        }

        /**
         * MÈthode qui rÈcupËre content type spÈcifique d'un message multipart...
         * (retourne uniquement un String, pour par exemple rÈcupÈrer le corp en HTML)
         *
         * @param multipart        Objet multipart
         * @param String         String ContentType, exemple text/html
         * @exception                 MessagingException, IOException Si une erreur survient.
         * @return String        Retourne un String contenant le corps (texte ASCII) du mail
         */
        public static String getBodyHandleMultipart(Multipart multipart, String contentType) throws MessagingException, IOException {
            String body = null;
            StringBuilder sb = new StringBuilder();

            // boucle sur les parties d'un message...
            for (int i = 0, n = multipart.getCount(); i < n; i++) {
                Part part = (Part) multipart.getBodyPart(i);
                Object content = part.getContent();
                if (content instanceof Multipart) // for multipart/mixed
                {
                    body = getBodyHandleMultipart((Multipart) content, contentType);
                    break;
                }

                String disposition = part.getDisposition();
                String ctype = part.getContentType();
                ContentType xctype = new ContentType(ctype.toLowerCase());

                if (xctype.match(contentType) && disposition == null) {
                    BufferedReader xreader = getTextReader(part);
                    for (String xline; (xline = xreader.readLine()) != null;) {
                        sb.append(xline).append('\n');
                    }
                    body = sb.toString();
                }
            }

            if (body == null) {
                throw new MessagingException("No " + contentType + " in the message!");
            }
            return body;
        }

        /**
         * MÈthode qui rÈcupËre le corps d'un mail
         *
         * @param part          Objet Part (partie d'un mail multipart)
         * @exception                   MessagingException, IOException Si une erreur survient.
         * @return BufferedReader      Retourne un BufferedReader contenant le corps (texte ASCII) du mail
         */
        public static BufferedReader getTextReader(Part part) throws MessagingException {
            try {
                InputStream xis = part.getInputStream();
                String ctype = part.getContentType();
                ContentType xct = new ContentType(ctype.toLowerCase());
                String xjcharset = xct.getParameter("charset");

                if (xjcharset != null) {
                    xjcharset = MimeUtility.javaCharset(xjcharset);
                } else {
                    // assume ASCII character encoding
                    xjcharset = MimeUtility.javaCharset("ASCII");
                }

                InputStreamReader inReader = null;

                try {
                    inReader = new InputStreamReader(xis, xjcharset);
                } catch (UnsupportedEncodingException ex) {
                    inReader = null;
                } catch (IllegalArgumentException ex) {
                    inReader = null;
                }

                if (inReader == null) {
                    xjcharset = MimeUtility.javaCharset("ASCII");
                    inReader = new InputStreamReader(xis, xjcharset);
                }

                BufferedReader xreader = new BufferedReader(inReader);
                return xreader;
            } catch (IOException xex) {
                throw new MessagingException(xex.toString());
            }
        }

        /**
         * MÈthode qui sauve un objet Message au format .elm
         *
         * @param mess
         * @param file_dest
         * @throws MessagingException
         * @throws IOException
         */
        public static void saveELM(MimeMessage mess, File file_dest) throws MessagingException, IOException {
            String message_id = encodeMessageID(mess.getMessageID());
            if ("".equals(message_id)) {
                message_id = "" + getMessageID(); // pas de Message-ID ???
            }
            PrintWriter out = new PrintWriter(new FileWriter(file_dest.getAbsolutePath() + a_sep + message_id + ".eml"), true);

            Enumeration e = mess.getAllHeaders();
            while (e.hasMoreElements()) {
                Header header = (Header) e.nextElement();
                out.println(header.getName() + ": " + header.getValue());
            }

            out.println();

            InputStream in = mess.getInputStream();
            BufferedReader a_br = new BufferedReader(new InputStreamReader(in, "8859_1"));
            String a_str = "";
            String a_strAux = "";
            while ((a_strAux = a_br.readLine()) != null) {
                a_str += a_strAux + "\n";
            }
            out.println(a_str);

            out.println();

            // flush & ferme le flux...
            out.flush();
            out.close();
        }

        /**
         * Retourne un objet Long reprÈsentant un Message-ID
         */
        public static Long getMessageID() {
            Long c_id = new Long(0);
            double d = java.lang.Math.random();
            c_id = new Long((long) (d * Long.MAX_VALUE));
            return c_id;
        }

        /**
         * MÈthode qui lit un fichier texte .eml et retourne qui un objet MimeMessage
         *
         * @param  String             chemin fichier .eml
         * @return MimeMessage         objet
         * @throws MessagingException Si un problËme de convertion ‡ la lecture du fichier arrive
         * @throws IOException
         */
        public static MimeMessage getMimeMessage(String file_eml) throws MessagingException, IOException {
            return getMimeMessage(new File(file_eml));
        }

        /**
         * MÈthode qui lit un fichier texte .eml et retourne qui un objet MimeMessage
         *
         * @param  File          fichier .eml
         * @return MimeMessage objet
         * @throws MessagingException Si un problËme de convertion ‡ la lecture du fichier arrive
         * @throws IOException
         */
        public static MimeMessage getMimeMessage(File f_eml) throws MessagingException, IOException {
            MimeMessage message = null;
            InputStream source = new FileInputStream(f_eml);
            message = new MimeMessage(null, source);
            return message;
        }

        /**
         * MÈthode qui encode le Message-ID pour Ítre compatible
         * avec un nom de fichier Window ou Unix (enlËve certains mÈta caractËres)
         *
         * @param str
         * @return Le message ID encodÈ
         */
        public static String encodeMessageID(String str) {
            String res = "";
            if (str == null) {
                return res;
            }
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                switch (c) {
                    case '<':
                        res += "";
                        break;
                    case '>':
                        res += "";
                        break;
                    case '\\':
                        res += "";
                        break;
                    case '/':
                        res += "";
                        break;
                    case '*':
                        res += "";
                        break;
                    case '?':
                        res += "";
                        break;
                    case '|':
                        res += "";
                        break;
                    case ':':
                        res += "";
                        break;
                    default:
                        res += c;
                }
            }
            return res;
        }
    }

    /**
     * Class interne gÈrant les types de fichier attachÈ ‡ un mail
     */
    public final static class FileBinary {

        private String c_file = "";
        private byte[] c_b = null;
        private int c_size = -1;
        private String c_header = "";

        /**
         * Constructeur...
         */
        public FileBinary(String _file) {
            this.c_file = _file;
            this.attach(_file);
        }

        public String getFile() {
            return c_file;
        }

        public String getFileName() {
            File f_file = new File(c_file);
            return f_file.getName();
        }

        private void attach(String c_file) {
            RandomAccessFile c_raf = null;
            try {
                c_raf = new RandomAccessFile(c_file, "rw");
                c_size = (int) c_raf.length();
                c_b = new byte[((c_size < 80) ? c_size : 80)];
                c_raf.readFully(c_b);
                c_header = new String(c_b, 0, c_b.length, "8859_1");
                c_raf.close();
            } catch (Throwable t) {
                try {
                    c_raf.close();
                } catch (Exception h) {
                }
            }
        }

        public boolean isExe() {
            if (c_size < 2) {
                return false;
            }
            return c_header.substring(0, 2).equals("MZ");
        }

        public boolean isPdf() {
            if (c_size < 4) {
                return false;
            }
            return c_header.substring(0, 4).equals("%PDF");
        }

        public boolean isAvi() {
            if (c_size < 4) {
                return false;
            }
            return c_header.substring(0, 4).equals("RIFF");
        }

        public boolean isGif() {
            if (c_size < 3) {
                return false;
            }
            return c_header.substring(0, 3).equals("GIF");
        }

        public boolean isBmp() {
            if (c_size < 3) {
                return false;
            }
            return c_header.substring(0, 3).equals("BMP");
        }

        public boolean isJpg() {
            if (c_b.length < 10) {
                return false;
            }
            return c_header.substring(6, 10).equalsIgnoreCase("JFIF");
        }

        public boolean isZip() {
            if (c_b.length < 2) {
                return false;
            }
            return c_header.substring(0, 2).equalsIgnoreCase("PK");
        }

        public boolean isMSOffice() {
            if (c_b.length < 8) {
                return false;
            }
            byte bytes[] = c_header.substring(0, 8).getBytes();
            String sbytes = tohexString(bytes, bytes.length, 0);
            return sbytes.equalsIgnoreCase("D0CF11E0A1B11AE1");
        }

        public boolean isWord() {
            if (c_file.lastIndexOf("doc") != -1) {
                return true;
            }
            return false;
        }

        public boolean isExcel() {
            if (c_file.lastIndexOf("xls") != -1) {
                return true;
            }
            return false;
        }

        public boolean isPPoint() {
            if (c_file.lastIndexOf("ppt") != -1) {
                return true;
            }
            return false;
        }

        public boolean isHtml() {
            if (c_file.lastIndexOf("htm") != -1 || c_file.lastIndexOf("html") != -1) {
                return true;
            }
            return false;
        }

        public boolean isTxt() {
            if (c_file.lastIndexOf("txt") != -1) {
                return true;
            }
            return false;
        }

        public boolean isSuffixe(String _ext) {
            if (_ext == null) {
                return false;
            }
            if (c_file.lastIndexOf(_ext) != -1) {
                return true;
            }
            return false;
        }

        private String tohexString(byte abyte0[], int i, int j) {
            StringBuilder sb = new StringBuilder(i * 2);
            for (int l = 0; l < i; l++) {
                int k;
                if (abyte0[l + j] < 0) {
                    k = abyte0[l + j] + 256;
                } else {
                    k = abyte0[l + j];
                }
                sb.insert(2 * l, halfBytetoHex(k / 16));
                sb.insert(2 * l + 1, halfBytetoHex(k - 16 * (k / 16)));
            }

            return sb.toString();
        }

        private char halfBytetoHex(int i) {
            if (i <= 9) {
                return (char) (48 + i);
            }
            if (i <= 15) {
                return (char) ((65 + i) - 10);
            } else {
                return '-';
            }
        }
    } // fin classe FileBinary

    /**
     * main...
     */
    public static void main(String[] args) {
        //Pop3 pop = new Pop3("c:\\temp", "pop.xxx.fr", "xxxx", "xxxx"); // protcole pop3 par defaut!
        Pop3 pop = new Pop3("c:\\temp", "xxxx.org", "xxxx", "xxxx", IMAPS_PORT, IMAP);

        pop.setSecurePOP3(true);
        pop.setDebug(true);
        pop.setSocketTimeout(5000);

        try {
            System.err.println("Start Job --- Pop3");

            // dÈbut lecture d'un fichier eml (texte)
      /*
            MimeMessage mime_mess = Pop3.Tools.getMimeMessage("c:\\temp\\002b01c7xxxa36c95f0$bb64668a@unixxxip85.eml");

            System.out.println("\tFrom : " + mime_mess.getFrom()[0]);
            System.out.println("\tSubject : " + mime_mess.getSubject());
            System.out.println("\tDate : " + mime_mess.getSentDate());
            Address[] address = mime_mess.getReplyTo();
            System.out.println("\tReplyTo : " + MimeUtility.decodeText(address[0].toString()));

            Vector vec_file_mess = Pop3.Tools.getFile(mime_mess);
            if (!vec_file_mess.isEmpty())
            {
            for (int j=0; j<vec_file_mess.size(); j++)
            {
            String a_file = (String) vec_file_mess.elementAt(j);

            // copier les fichiers hors du dossier temporaire!!!
            System.out.println("\tPathFileName : " + a_file);
            }
            }
            else System.out.println("\tPas de fichier en attachement!");

            // le body
            try
            {
            String body = Pop3.Tools.getBody(mime_mess);
            System.out.println("\n\tBody : " + body);
            }
            catch(Exception e) { System.out.println("\n\tBody : " + e); }

            // body html pour les mail au format html uniquement...
            try
            {
            String bodyhtml = Pop3.Tools.getBodyHtml(mime_mess);
            System.out.println("\n\tBodyHtml: " + bodyhtml);
            }
            catch(Exception e) { System.out.println("\n\tBodyHtml : " + e); }
             */
            // fin lecture d'un fichier eml (texte)

            // POP3 mode : INBOX par defaut
            // IMAP4 mode : INBOX par defaut, dispo normalement : Drafts, Sent, Trash
            Message[] pop_message = pop.getMail(true); //  dossier courant "INBOX", true debug

            /*
            // IMAP4 liste tous les dossiers du folder courant
            //UIDFolder ufolder = (UIDFolder) pop.getFolder();
            //System.err.println("MESSAGE UID :" + ufolder.getUID(pop_message[i]));

            Folder[] xfolders = pop.getRootFolders();
            if (xfolders ==null || xfolders.length == 0) System.err.println("***  IMAP Root Folder : ?? ");
            else
            {
            System.err.println("**** IMAP Root Folder : ");
            for (int i=0, n=xfolders.length; i<n; i++)
            System.err.println("\tFolder : -"+i+" "+xfolders[i].getName());
            }

            // dossier courant
            xfolders = pop.getFolders();
            if (xfolders ==null || xfolders.length == 0) System.err.println("***  NO Folder in current folder!");
            else
            {
            System.err.println("**** Folder : ");
            for (int i=0, n=xfolders.length; i<n; i++)
            System.err.println("\tFolder : -"+i+" "+xfolders[i].getName());
            }

            // change de dossier...
            //pop_message = pop.getMail("Test"+pop.getSep()+"SubTest", true); //  dossier courant "INBOX", true debug
            pop_message = pop.getMail("Test", true); //  dossier courant "INBOX", true debug

            xfolders = pop.getRootFolders();
            if (xfolders ==null || xfolders.length == 0) System.err.println("***  IMAP Root Folder : ?? ");
            else
            {
            System.err.println("**** IMAP Root Folder : ");
            for (int i=0, n=xfolders.length; i<n; i++)
            System.err.println("\tFolder : -"+i+" "+xfolders[i].getName());
            }

            // dossier courant
            xfolders = pop.getFolders();
            if (xfolders ==null || xfolders.length == 0) System.err.println("***  NO Folder in current folder!");
            else
            {
            System.err.println("**** Folder : ");
            for (int i=0, n=xfolders.length; i<n; i++)
            System.err.println("\tFolder : -"+i+" "+xfolders[i].getName());
            }

            Folder[] subcr_folder = pop.getSubscribedFolder();
            if (subcr_folder == null || subcr_folder.length == 0) System.err.println("*** NO Folder Subcribe in current folder!");
            else
            {
            for (int i=0, n=subcr_folder.length; i<n; i++)
            System.err.println("************ Folder Subcribe : -"+i+" "+xfolders[i].getName());
            }

            Folder[] unsubcr_folder = pop.getUnSubscribedFolder();
            if (unsubcr_folder == null || unsubcr_folder.length == 0) System.err.println("*** NO Folder UnSubcribe in current folder!");
            else
            {
            for (int i=0, n=unsubcr_folder.length; i<n; i++)
            System.err.println("************ Folder UnSubcribe : -"+i+" "+xfolders[i].getName());
            }
             */

            // IMAP4 crÈation, copie & destruction (PERMANENT) de dossier
      /*
            System.out.println("*** DELETE FOLDER : "+ pop.deleteFolder("Test"));
            System.out.println("*** CREATE FOLDER : "+ pop.createFolder("Test"));
            System.out.println("*** COPY FOLDER : "+ pop.copyFolderMessages("Trash", "Test"));
             */

            //System.out.println("*** SUBCRIBE/UNSUBCRIBE FOLDER : "+ pop.setSubscribed("Test", false));
            //System.out.println("*** RENAME FOLDER : "+  pop.renameFolder("Test", "Testy"));
            //System.out.println("*** DELETE FOLDER : "+ pop.deleteFolder("Testy"));
            //System.out.println("*** COPY FOLDER : "+ pop.copyFolderMessages("Test", "SubTest"));

            // IMAP4 liste tous les dossiers de Test
            //pop_message = pop.getMail("Test", true); // on change de dossier courant pour root
            //System.out.println("*** CREATE FOLDER : "+ pop.createFolder("Test.SubTest"));
            //System.out.println("*** SUBCRIBE/UNSUBCRIBE FOLDER : "+ pop.setSubscribed("Test.SubTest", true));
            //System.out.println("*** DELETE FOLDER : "+ pop.deleteFolder("Test.SubTest"));


            for (int i = 0, n = pop_message.length; i < n; i++) {
                //pop_message[i].writeTo(System.out);

                System.err.println("\tFrom : " + pop_message[i].getFrom()[0]);
                System.err.println("\tSubject : " + pop_message[i].getSubject());
                System.err.println("\tDate : " + pop_message[i].getSentDate());
                Address[] add = pop_message[i].getReplyTo();
                System.err.println("\tReplyTo : " + add[0].toString());
                /*
                MimeMessage mess = (MimeMessage) pop_message[i];

                String[] strTo = mess.getHeader("To");
                if (strTo !=null) // ?
                for (int j=0, m=strTo.length; j<m; j++)
                System.out.println("\tTo : " + strTo[j]);

                String[] strReceived = mess.getHeader("Received");
                if (strReceived !=null) // impossible
                for (int j=0, m=strReceived.length; j<m; j++)
                System.out.println("\tReceived : " + strReceived[j]);

                System.out.println("\tMessage-ID : " + mess.getMessageID());


                //Enumeration e = mess.getAllHeaders();
                //while (e.hasMoreElements())
                //{
                //        Header header = (Header) e.nextElement();
                //        System.out.println(header.getName() +" : "+header.getValue());
                //}


                // le body
                try
                {
                String body = Pop3.Tools.getBody(pop_message[i]);
                System.out.println("\n\tBody : " + body);
                }
                catch(Exception e) { System.out.println("\n\tBody : " + e); }


                // body html pour les mail au format html uniquement...
                try
                {
                String bodyhtml = Pop3.Tools.getBodyHtml(pop_message[i]);
                System.out.println("\n\tBodyHtml: " + bodyhtml);
                }
                catch(Exception e) { System.out.println("\n\tBodyHtml : " + e); }

                // les fichiers en attachement..
                Vector vec_file = Pop3.Tools.getFile(pop_message[i]);
                if (!vec_file.isEmpty())
                {
                for (int j=0; j<vec_file.size(); j++)
                {
                String a_file = (String) vec_file.elementAt(j);

                // copier les fichiers hors du dossier temporaire!!!
                System.out.println("\tPathFileName : " + a_file);

                Pop3.FileBinary fb = new Pop3.FileBinary(a_file);
                System.out.println("\t isPdf : " + fb.isPdf());
                System.out.println("\t isMSOffice : " + fb.isMSOffice());

                }
                }
                else System.out.println("\tPas de fichier en attachement!");

                // les fichiers embarquÈs (dans l'html ou autre...)
                Vector vec_file_embed = Pop3.Tools.getFileEmbed(pop_message[i], "image/jpeg");
                if (!vec_file_embed.isEmpty())
                {
                for (int j=0; j<vec_file_embed.size(); j++)
                {
                String a_file = (String) vec_file_embed.elementAt(j);
                // copier les fichiers hors du dossier temporaire!!!
                System.out.println("\tPathFileName Embed: " + a_file);
                }
                }
                else System.out.println("\tPas de fichier embarquÈ!");

                System.out.println();

                // lecture des Flags IMAP4
                System.out.println("Count read : "+pop.countMessageRead());
                System.out.println("isNew : "+pop.isNew(i));
                System.out.println("isRead : "+pop.isRead(i));

                // sauve le message dans un rÈpertoire dÈdier...
                //Pop3.Tools.saveELM(mess, new File("c:\\temp"));

                System.out.println("isConnected : " + pop.store.isConnected()); // Noop

                // on se prepare ‡ effacer les mails!
                //pop_message[i].setFlag(Flags.Flag.DELETED, false); // true
                 */

                System.out.println();
                System.out.println();

            }

            System.out.println("End Job --- Pop3");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Pile : ");
            e.printStackTrace();
        } finally {
            // ferme la session pop3 et efface les messages flagger 'DELETED' !
            try {
                pop.close(true);
            } catch (MessagingException me) {
            }
        }
    }
} // fin de classe
