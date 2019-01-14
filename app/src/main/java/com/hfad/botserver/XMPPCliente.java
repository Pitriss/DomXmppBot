package com.hfad.botserver;

import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.httpfileupload.HttpFileUploadManager;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.si.packet.StreamInitiation;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class XMPPCliente {

    private static XMPPCliente ptr_xmpp = null;
    private AbstractXMPPConnection connection;
    private ChatManager chatManager;
    private boolean isConnected = false;
    ArrayList<String> RosterJids=new ArrayList<String>();
    private boolean alarmaArmada = true;
    private String alarmaClave = "1234";

    private XMPPCliente() {
    }

    public static XMPPCliente getInstance()
    {
        if(ptr_xmpp == null)
        {
            ptr_xmpp = new XMPPCliente();
        }
        return ptr_xmpp;
    }

    public static void setInstance(XMPPCliente instance)
    {
        ptr_xmpp = instance;
    }

    public void checkIsConnected(final String user, final String pass, final String domain) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(connection.isAuthenticated())
                {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runLoggin(user,pass,domain);
            }
        };

        thread.start();
    }

    public void runLoggin(final String user, final String pass, final String domain)
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                isConnected = false;
                try {
                    connection = new XMPPTCPConnection(user, pass, domain);
                    connection.connect().login();

                    chatManager = ChatManager.getInstanceFor(connection);

                    chatManager.addIncomingListener(new IncomingChatMessageListener() {
                        @Override
                        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                            System.out.println("New message from " + from + ": " + message.getBody());
                            String body = message.getBody();
                            String[] split = body.split(" ");
                            if (((split[0].equals("/alarmaActivar")) || (split[0].equals("/alarmaEstado")) || (split[0].equals("/alarmaDesactivar")))&& (split[1].equals(alarmaClave))){
                                if(split[0].equals("/alarmaActivar")) {
                                    alarmaArmada = true;
                                }else if(split[0].equals("/alarmaDesactivar")){
                                    alarmaArmada = false;
                                }
                                sendMsj(from.toString(),"El estado actual de la alarma es " + String.valueOf(alarmaArmada));
                            }
                            else if((split[0].equals("/foto")) && (split[1].equals(alarmaClave)))
                            {
                                new Thread(new sendPicture()).start();
                            }
                            else {
                                Nodos.getInstance().processNodoData(from.toString(), message.getBody());
                            }

                        }
                    });

                    loadRoster();
                    isConnected = true;

                }
                catch (XmppStringprepException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
                finally {
                    //connectionFinished();
                    if(!isConnected)
                    {
                        runLoggin(user,pass,domain);
                    }
                    else
                    {
                        checkIsConnected(user,pass,domain);
                    }
                }
            }
        };

        thread.start();
    }

    private class sendPicture extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                sendFile(Server.getInstance().takePhoto());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadRoster()
    {
        RosterJids.clear();
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry.toString()+"/"+entry.getJid().getResourceOrEmpty().toString());
            RosterJids.add(entry.toString()+"/"+entry.getJid().getResourceOrEmpty().toString());
        }

        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<Jid> addresses) {
                System.out.println("Presence entry: " + addresses.toString());
            }

            @Override
            public void entriesUpdated(Collection<Jid> addresses) {
                System.out.println("Presence update: " + addresses.toString());
            }

            @Override
            public void entriesDeleted(Collection<Jid> addresses) {

            }

            public void presenceChanged(Presence presence) {
                System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
            }
        });
    }

    public boolean sendMsj(String user, String msj)
    {
        if(isConnected) {
            EntityBareJid jid = null;
            try {
                jid = JidCreate.entityBareFrom(user);
                Chat chat = chatManager.chatWith(jid);
                chat.send(msj);
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            return  false;
        }
        return true;
    }

    public boolean sendMsj(String msj)
    {
        if(isConnected) {
            EntityBareJid jid = null;
            try {
                for(String user:RosterJids) {
                    jid = JidCreate.entityBareFrom(user);
                    Chat chat = chatManager.chatWith(jid);
                    chat.send(msj);
                }
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            return  false;
        }
        return true;
    }

    public boolean sendMessage(String subject, String body)
    {

        if(isConnected) {
            EntityBareJid jid = null;
            try {
                for(String user:RosterJids) {
                    jid = JidCreate.entityBareFrom(user);
                    Chat chat = chatManager.chatWith(jid);
                    Message message = new Message(jid);
                    message.setBody(body);
                    //message.setSubject(subject);
                    JivePropertiesManager.addProperty(message, "url", body);
                    chat.send(message);
                }
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            return  false;
        }
        return true;
    }

    public boolean isAlarmaArmada()
    {
        return alarmaArmada;
    }

    public void sendFile(String filepath) {
        HttpFileUploadManager manager = HttpFileUploadManager.getInstanceFor(connection);
        File file = new File(filepath);
        try {
            if(manager.discoverUploadService())
            {
                URL url = manager.uploadFile(file);
                sendMessage("<x xmlns='jabber:x:oob'>\n" +
                        "<url>" + url.toString() + "</url>\n" +
                        "</x>",url.toString());
            }
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }


/*// Create the file transfer manager
        FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
// Create the outgoing file transfer
        OutgoingFileTransfer transfer = null;
        File file = new File(filepath);
        Log.i("XMPP",file.toString());
        try {
            transfer = manager.createOutgoingFileTransfer(JidCreate.entityFullFrom("elcuco@404.city/Pix-Art Messenger.rbjE"));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
// Send the file
        try {
            transfer.sendFile(file, "You won't believe this!");
        } catch (SmackException e) {
            e.printStackTrace();
        }
*/
    }

}
