package com.hfad.botserver;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class XMPPCliente {

    private static XMPPCliente ptr_xmpp = null;
    private AbstractXMPPConnection connection;
    private ChatManager chatManager;
    private boolean isConnected = false;
    ArrayList<String> RosterJids=new ArrayList<String>();

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

    public void checkIsConnected(final String user, final String pass, final String domain) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(connection.isAuthenticated())
                {
                    try {
                        Thread.sleep(30000);
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
                            //String body = message.getBody();
                            //String [] split = body.split(" ");
                            Nodos.getInstance().processNodoData(from.toString(),message.getBody());

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

    private void loadRoster()
    {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry);
            RosterJids.add(entry.toString());
        }
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

}
