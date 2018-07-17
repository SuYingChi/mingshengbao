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

        public class ServeCategory{
            public String name;
            public String code;
            public ArrayList<ChildCategory> child;
            public class ChildCategory{
                public int    id;
                public String name;
                public String code;
            }
        }
    }
}
