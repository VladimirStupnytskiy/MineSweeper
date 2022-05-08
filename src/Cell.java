import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Vladimir on 04.02.2016.
 */

public class Cell extends JButton{

    private boolean isBomb = false;
    int radiusBomb = 0;
    int nIndex = 0;
    int mIndex = 0;
    String text = "";
    private boolean ifChecked = false;
    private boolean flag = false;
    private boolean editable = true; // protection against repetable markers


    public Boolean getFlag() {
        return flag;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Cell(int nIndex, int mIndex) {
        this.nIndex = nIndex;
        this.mIndex = mIndex;
    }

    public void setTextt(String str){
        this.setText(str);
    }

    public boolean isBomb() {
        return isBomb;
    }

    public int getRadiusBomb() {
        return radiusBomb;
    }

    public void setRadiusBomb(int radiusBomb) {
        this.radiusBomb = radiusBomb;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    public void incBomb() { radiusBomb++; }



    public String getSecret(){
        if (isBomb){
            return "x";
        }else return Integer.toString(radiusBomb);


    }

    public int getnIndex() {
        return nIndex;
    }

    public int getmIndex() {
        return mIndex;
    }

    public boolean getIfChecked() {
        return ifChecked;
    }

    public void setIfChecked(boolean ifChecked) {
        this.ifChecked = ifChecked;
    }


}

