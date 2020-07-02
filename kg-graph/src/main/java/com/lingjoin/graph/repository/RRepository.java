package com.lingjoin.graph.repository;

import com.lingjoin.graph.gentity.Relationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RRepository extends Neo4jRepository<Relationship,Long> {




    @Query("match(x)-[y]-(z) where x.name={startName} and y.name={rName} and z.name={endName}  return y ")
    Relationship findBynametoname(@Param("startName") String startName,
                                  @Param("rName") String rName,
                                  @Param("endName") String endName);


    @Query("match(x)-[y]-(z) where x.uid={uid} and y.uid={uid} and z.uid={uid} " +
            "and x.key={key} and y.name={rName} and z.key={endkey} and x.uuid is null and z.uuid is null  return y")
    Relationship findBykeytokey(@Param("key") String key,
                                @Param("rName") String rName,
                                @Param("endkey") String endkey,
                                @Param("uid") Integer uid);




    @Query("match(x)-[y]-(z) where x.uid={uid} and y.uid={uid} and z.uid={uid} " +
            "and x.name={startName} and y.name={rName} and z.name={endName} and x.uuid={uuid} and z.uuid={uuid} return y" )
    Relationship findBynametonamewithuuid(@Param("startName") String startName,
                                          @Param("rName") String rName,
                                          @Param("endName") String endName,
                                          @Param("uid") Integer uid,
                                          @Param("uuid") String uuid);






    @Query("match(x)-[y:Relationship]-(z) where y.uid={uid}  and x.uuid is null and z.uuid is null return x,y,z  ")
    List<Relationship> allRel(@Param("uid") Integer uid);

    @Query("match(x)-[y:Relationship]-(z) where y.uid={uid} and x.stru='1'  return x,y,z limit 150")
    List<Relationship> rels(@Param("uid") Integer uid);

    @Query("match(x)-[y:Relationship]-(z) where y.uid={uid} and x.level=1  return x,y,z limit 150")
    List<Relationship> initRels(@Param("uid") Integer uid);




    @Query("match(x)-[y:Relationship]-(z) where y.uid={uid} and x.key<>'' and x.uuid is null  return x,y,z ")
    List<Relationship> mainRels(@Param("uid") Integer uid);


    @Query("match(x)-[y:Relationship]-(z) where x.level=1 and x.uuid={uuid}  return x,y,z ")
    List<Relationship> sinMainRels(@Param("uid") Integer uid, @Param("uuid") String uuid);


    @Query("match(x)-[y:Relationship]-(z) where x.uuid={uuid} and z.uuid={uuid} return x,y,z")
    List<Relationship> allRelwithUUID(@Param("uuid") String uuid);

    @Query("match (p)-[r]-() where r.uid={uid}  and p.name=~{name} and p.uuid is null return r")
    List<Relationship>findByname(@Param("name") String name, @Param("uid") Integer uid);


    @Query("MATCH (x),(y),p=shortestpath((x)-[*..5]-(y)) where x.uuid is null and y.uuid is null and x.name=~{name1} and y.name=~{name2} and x.uid={uid} and y.uid={uid} RETURN p")
    List<Relationship> shortestPath(@Param("name1") String name1,
                                    @Param("name2") String name2,
                                    @Param("uid") Integer uid);



}
