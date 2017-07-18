package tcm.com.gistone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tcm.com.gistone.database.mapper.NounMapper;
import tcm.com.gistone.util.EdatResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class NounController {
    @Autowired
    private NounMapper nm;
   /* @RequestMapping(value="Noun/recordNoun",method = RequestMethod.POST)
    public EdatResult recordNoun(HttpServletRequest request, HttpServletResponse response){

        return null;
    }*/
	
}
