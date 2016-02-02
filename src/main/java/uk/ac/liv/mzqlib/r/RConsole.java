package uk.ac.liv.mzqlib.r;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Da Qi
 * @institute University of Liverpool
 * @time 30-Jul-2014 15:48:16
 */
public class RConsole implements RMainLoopCallbacks {

    @Override
    public void rWriteConsole(Rengine re, String text, int oType) {
        System.out.print(text);
    }

    @Override
    public void rBusy(Rengine re, int which) {
        System.out.println("rBusy(" + which + ")");
    }

    @Override
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        System.out.print(prompt);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s = br.readLine();
            return (s == null || s.length() == 0) ? s : s + "\n";
        }
        catch (Exception e) {
            System.out.println("jriReadConsole exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void rShowMessage(Rengine re, String message) {
        System.out.println("rShowMessage \"" + message + "\"");
    }

    @Override
    public String rChooseFile(Rengine re, int newFile) {
        FileDialog fd = new FileDialog(new Frame(), (newFile == 0) ? "Select a file" : "Select a new file", (newFile == 0) ? FileDialog.LOAD : FileDialog.SAVE);
        fd.setVisible(true);
        String res = null;
        if (fd.getDirectory() != null) {
            res = fd.getDirectory();
        }
        if (fd.getFile() != null) {
            res = (res == null) ? fd.getFile() : (res + fd.getFile());
        }
        return res;
    }

    @Override
    public void rFlushConsole(Rengine re) {
    }

    @Override
    public void rLoadHistory(Rengine re, String filename) {
    }

    @Override
    public void rSaveHistory(Rengine re, String filename) {
    }

}
