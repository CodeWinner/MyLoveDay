package ngokhacbac.mydaylove;

import java.util.ArrayList;

/**
 * Created by pc1 on 11/2/2017.
 */

public class Model_KyNiem {
    private ArrayList<String> listMess;
    private ArrayList<String> listPathAnh;
    private ArrayList<String> listDay;

    public Model_KyNiem(ArrayList<String> listMess, ArrayList<String> listPathAnh, ArrayList<String> listDay) {
        this.listMess = listMess;
        this.listPathAnh = listPathAnh;
        this.listDay = listDay;
    }

    public ArrayList<String> getListMess() {
        return listMess;
    }

    public void setListMess(ArrayList<String> listMess) {
        this.listMess = listMess;
    }

    public ArrayList<String> getListPathAnh() {
        return listPathAnh;
    }

    public void setListPathAnh(ArrayList<String> listPathAnh) {
        this.listPathAnh = listPathAnh;
    }

    public ArrayList<String> getListDay() {
        return listDay;
    }

    public void setListDay(ArrayList<String> listDay) {
        this.listDay = listDay;
    }
    public String toStringListMess()
    {
        String mess = "";
        if(listMess.size()!=0)
        {
            for (int i=0;i<listMess.size();i++)
            {
                mess = mess+listMess.get(i)+",";
            }
        }
        return mess;
    }
    public String toStringListDay()
    {
        String mess = "";
        if(listDay.size()!=0)
        {
            for (int i=0;i<listDay.size();i++)
            {
                mess = mess+listDay.get(i)+",";
            }
        }
        return mess;
    }
    public String toStringListPath()
    {
        String mess = "";
        if(listPathAnh.size()!=0)
        {
            for (int i=0;i<listPathAnh.size();i++)
            {
                mess = mess+listPathAnh.get(i)+",";
            }
        }
        return mess;
    }
}
