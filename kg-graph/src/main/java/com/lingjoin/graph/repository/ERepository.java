package com.lingjoin.graph.repository;


import com.lingjoin.graph.gentity.Entity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ERepository extends Neo4jRepository<Entity,Long> {

    @Query("match(x:Entity) where x.uid={0} and x.uuid is null return x")
    List<Entity> totalEntity(Integer uid);

    @Query("match(x:Entity) where x.uid={0} and x.level=1 or x.level=2 return x")
    List<Entity> entity1to2(Integer uid);

    @Query("match(x)-[y:Relationship]-(z) where y.uid={uid} and x.key<>'' and x.uuid is null  return x,z")
    List<Entity> mainEntity(@Param("uid") Integer uid);

}
