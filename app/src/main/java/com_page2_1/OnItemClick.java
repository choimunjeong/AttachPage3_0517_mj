package com_page2_1;

import java.util.ArrayList;

public interface OnItemClick {
    void onClick(double x, double y, String name);
    void make_db(String countId, String name, String cityname);
    void delete_db(String contentId);
    void make_dialog();

}
