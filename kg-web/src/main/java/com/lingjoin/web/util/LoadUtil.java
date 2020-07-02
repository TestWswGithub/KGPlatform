package com.lingjoin.web.util;


import com.lingjoin.graph.gentity.Entity;
import com.lingjoin.graph.gentity.Relationship;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadUtil {

    private static final String[] COLOR_POOL = {"#FFD700", "#C71585", "#66FF00", "#FF3366"};
    private static Random random = new Random();

    public static void load(List<Relationship> rlist, ArrayList<NewRDTO> newRDTOS) {

        for (Relationship r : rlist) {
            NewRDTO rdto = new NewRDTO(r.getFrom().getMain()+r.getFrom().getName(),
                    r.getTo().getMain()+r.getTo().getName(), r.getName(), null,
                    null, null);



            rdto.setLineStyle(
                    new NewRDTO.LineStyle(new NewRDTO.LineStyle.Normal(COLOR_POOL[random.nextInt(4)]))
            );


            newRDTOS.add(rdto);

        }


    }

    public static ArrayList<NodeDTO> nodeTrans(List<Entity> entities) {

        ArrayList<NodeDTO> nodes = new ArrayList<>();

        for (Entity e : entities) {
            NodeDTO nodeDTO = new NodeDTO(e.getName(), e.getInfo(), e.getMain()+e.getName(), null);
            Normal normal =null;
            if (e.getMain()==0) normal=new Normal("#FFD700");
            else normal=new Normal(COLOR_POOL[random.nextInt(4)]);
            nodeDTO.setItemStyle(new ItemStyle(normal));
            nodes.add(nodeDTO);
        }
        return nodes;

    }


}
