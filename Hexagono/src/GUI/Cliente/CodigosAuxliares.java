/**
 * ChatClient.java  1.00 96/11/07 Merlin Hughes
 *
 * Copyright (c) 1996 Prominence Dot Com, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * for non-commercial purposes and without fee is hereby granted
 * provided that this copyright notice appears in all copies.
 *
 * http://prominence.com/                  merlin@prominence.com
 */
 
import java.net.*;
import java.io.*;
import java.awt.*;

public class ChatClient extends Frame implements Runnable {
  protected DataInputStream i;
  protected DataOutputStream o;

  protected TextArea output;
  protected TextField input;

  protected Thread listener;

  public ChatClient (String title, InputStream i, OutputStream o) {
    super (title);
    this.i = new DataInputStream (new BufferedInputStream (i));
    this.o = new DataOutputStream (new BufferedOutputStream (o));
    setLayout (new BorderLayout ());
    add ("Center", output = new TextArea ());
    output.setEditable (false);
    add ("South", input = new TextField ());
    pack ();
    show ();
    input.requestFocus ();
    listener = new Thread (this);
    listener.start ();
  }

  public void run () {
    try {
      while (true) {
        String line = i.readUTF ();
        output.appendText (line + "\n");
      }
    } catch (IOException ex) {
      ex.printStackTrace ();
    } finally {
      listener = null;
      input.hide ();
      validate ();
      try {
        o.close ();
      } catch (IOException ex) {
        ex.printStackTrace ();
      }
    }
  }

  public boolean handleEvent (Event e) {
    if ((e.target == input) && (e.id == Event.ACTION_EVENT)) {
      try {
        o.writeUTF ((String) e.arg);
        o.flush ();
      } catch (IOException ex) {
        ex.printStackTrace();
        listener.stop ();
      }
      input.setText ("");
      return true;
    } else if ((e.target == this) && (e.id == Event.WINDOW_DESTROY)) {
      if (listener != null)
        listener.stop ();
      hide ();
      return true;
    }
    return super.handleEvent (e);
  }

  public static void main (String args[]) throws IOException {
    if (args.length != 2)
      throw new RuntimeException ("Syntax: ChatClient <host> <port>");

    Socket s = new Socket (args[0], Integer.parseInt (args[1]));
    new ChatClient ("Chat " + args[0] + ":" + args[1],
                    s.getInputStream (), s.getOutputStream ());
  }
}


import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread 
{
    protected Socket s;
    protected DataInputStream i;
    protected DataOutputStream o;

    public ChatHandler (Socket s) throws IOException 
    {
        this.s = s;
        i = new DataInputStream (new BufferedInputStream (s.getInputStream ()));
        o = new DataOutputStream (new BufferedOutputStream (s.getOutputStream ()));
    }
    
    protected static Vector handlers = new Vector ();

    public void run ()
    {
        String name = s.getInetAddress ().toString ();
        try 
        {
            broadcast (name + " has joined.");
            handlers.addElement (this);
        
            while (true) 
            {
                String msg = i.readUTF ();
                broadcast (name + " - " + msg);
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace ();
        }
        finally 
        {
            handlers.removeElement (this);
            broadcast (name + " has left.");
        
            try
            {
                s.close ();
            }
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
    }
  }

  protected static void broadcast (String message) {
    synchronized (handlers) {
      Enumeration e = handlers.elements ();
      while (e.hasMoreElements ()) {
        ChatHandler c = (ChatHandler) e.nextElement ();
        try {
          synchronized (c.o) {
            c.o.writeUTF (message);
          }
          c.o.flush ();
        } catch (IOException ex) {
          c.stop ();
        }
      }
    }
  }
}


import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
  public ChatServer (int port) throws IOException {
    ServerSocket server = new ServerSocket (port);
    while (true) {
      Socket client = server.accept ();
      System.out.println ("Accepted from " + client.getInetAddress ());
      ChatHandler c = new ChatHandler (client);
      c.start ();
    }
  }

  public static void main (String args[]) throws IOException {
    if (args.length != 1)
      throw new RuntimeException ("Syntax: ChatServer <port>");
    new ChatServer (Integer.parseInt (args[0]));
  }
}
