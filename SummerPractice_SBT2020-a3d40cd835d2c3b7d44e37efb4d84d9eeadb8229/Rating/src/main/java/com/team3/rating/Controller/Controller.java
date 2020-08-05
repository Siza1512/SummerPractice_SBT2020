package com.team3.rating.Controller;

import com.team3.rating.Database.RatingDataOperator;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rating")
public class Controller {
    @Autowired
    private RatingDataOperator ratingDataOperator;

    @PostMapping("/")
    public HttpStatus ratePost(@RequestBody Document requestBody) {
        requestBody.remove("token");
        ratingDataOperator.createRating(requestBody);

        return HttpStatus.OK;
    }


    @GetMapping("collections/{collectionID}/posts/{postID}/users/{userID}/criterion/{criterionName}")
    public String getRatingByCriterion(@PathVariable Integer collectionID,
                                       @PathVariable Integer postID,
                                       @PathVariable Integer userID,
                                       @PathVariable String criterionName) {
        return collectionID + "," + postID + "," +  userID;
    }


    @GetMapping("average/collections/{collectionID}/posts/{postID}/criterion/{criterionName}")
    public String getAverageRatingByCriterion(@PathVariable Integer collectionID,
                                              @PathVariable Integer postID,
                                              @PathVariable String criterionName) {
        return new Document("цена:" , 5).toJson();
    }


    @DeleteMapping("collections/posts/{postID}/token/{token}")
    public ResponseEntity<String> deleteAllPostRatings(@PathVariable String token,
                                        @PathVariable Integer postID) {
        boolean canDeleteAllPostRatings = token.equals("1");
        if (canDeleteAllPostRatings) {
            ratingDataOperator.deleteAllPostRatings(postID);
        }
        else {
            return new ResponseEntity<>("Not enough rights", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping("collections/{posts}/token/{token}")
    public ResponseEntity<String> deleteAllPostsAllRatings(@PathVariable("posts") int postsList[],
                                                            @PathVariable("token") String token) {
        boolean canDeleteAllPostsAllRatings = token.equals("1");
        if (canDeleteAllPostsAllRatings) {
            ratingDataOperator.deleteAllPostsAllRatings(postsList);
        }
        else {
            return new ResponseEntity<>("Not enough rights", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/average/collections/{collectionID}/posts/{postID}")
    public ResponseEntity<Document> getAveragePostRating(@PathVariable("collectionID") String collectionID,
                                       @PathVariable("postID") String postID){
        String currentCollectionID = collectionID;
        if (true) {
            Document doc = ratingDataOperator.getAveragePostRating(postID);
            if(doc == null){
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }else{
                return new ResponseEntity<>(doc, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
