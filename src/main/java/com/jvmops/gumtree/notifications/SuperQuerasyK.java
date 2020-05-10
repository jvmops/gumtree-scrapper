package com.jvmops.gumtree.notifications;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * At first I was like - lets do mongo! I have no prior experience with that
 * and docs states that it has text index capabilities, I should be fine with
 * my simple use case!
 *
 * Man, I was so wrong...
 *
 * Mongo doesn't support polish for diacritics and you can't do "like" queries
 * against a text index. That is why I had to go with regexp over a regular
 * field. End result looks ridiculous.
 *
 * But fuck it. I'm keeping the mongo here. Let it be the lesson for us. If you
 * need anything more than basic search from a text index just stick with lucene.
 */
@Repository
public interface SuperQuerasyK extends MongoRepository<Ad, ObjectId> {

    @Query(value = """
            { $and:
                [{
                    description: {
                        $regex: "((zmywark.*)(gaz.*))|((gaz.*)(zmywark.*))"
                    }
                },
                {
                    city: ?0
                },
                {
                    gumtreeCreationDate: {
                        $gt: ?1
                    }
                }]
            }""",
            sort = """
            { 
                gumtreeCreationDate: -1,
                price: 1
            }"""
    )
    List<Ad> findAllByCityWithDishwasherAndGas(String city, LocalDate gumtreeCreationDate);

    @Query(value = """
            { $and: 
                [{
                    description: {
                        $regex: "zmywark"
                    }
                },
                {
                    description: {
                        $regex: "^((?!gaz).)*$"
                    }
                },
                {
                    city: ?0
                }
                {
                    gumtreeCreationDate: {
                        $gt: ?1
                    }
                }]
            }""",
            sort = """
            { 
                gumtreeCreationDate: -1,
                price: 1
            }"""
    )
    List<Ad> findAllByCityWithDishwasherOnly(String city, LocalDate gumtreeCreationDate);
}