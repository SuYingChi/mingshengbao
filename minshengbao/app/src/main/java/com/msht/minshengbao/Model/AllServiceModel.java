package com.msht.minshengbao.Model;

import java.util.ArrayList;

/**
 * Created by hong on 2017/11/10.
 */

public class AllServiceModel extends BaseModel{
    public MainCategory data;
    public static class MainCategory{
        public int city_id;
        public int online_flag;
        public ArrayList<ServeCategory> serve;

        public static class ServeCategory{
            public String name;
            public String code;
            public ArrayList<ChildCategory> child;
            public static class ChildCategory{
                public int    id;
                public String name;
                public String code;
                public String url;

                public ChildCategory(String name) {
                    this.name = name;
                }
            }
        }
    }
}
