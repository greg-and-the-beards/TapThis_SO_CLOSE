package com.tapthis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.util.UriComponentsBuilder;

import com.tapthis.entity.ReviewInfo;
import com.tapthis.entity.UserInfo;
import com.tapthis.service.ReviewService;
import com.tapthis.service.UserService;

import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebsiteController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReviewService reviewService;

	// index - home page when logged out
	@RequestMapping("/")
	public ModelAndView home(ModelAndView mv) {
		mv.setViewName("index");
		return mv;
	}

	// HTTP SESSION user search results page with multiple beers
	@RequestMapping("/APIResults")
	public ModelAndView APIResults(ModelAndView mv) {
		return mv;
	}
	
	// LOGGED OUT user search results page with multiple beers
	@RequestMapping("/searchResults")
	public ModelAndView searchResults(ModelAndView mv) {
		return mv;
	}
	
	// LOGGED OUT specific beer selected from searchResults page
	@RequestMapping("/yourBeer")
	public ModelAndView yourBeer(ModelAndView mv) {
		return mv;
	}

	// HTTP SESSION specific beer selected from APIResults page
	@RequestMapping("/beerDetails")
	public ModelAndView beerDetails(ModelAndView mv) {
		return mv;
	}

	// registration page form for writing one user to database
	@RequestMapping("/register")
	public ModelAndView register(ModelAndView mv) {
		return mv;
	}
	
	// home page when logged in
	@RequestMapping("/home")
	public ModelAndView loggedInIndex(ModelAndView mv) {
		return mv;
	}
	
	// POST one user to user table with HTTP session initiation
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ModelAndView eachUser(@ModelAttribute UserInfo user, ModelAndView mv, HttpSession sessionObj) {
		userService.addUser(user);
		sessionObj.setAttribute("user", user);
		return new ModelAndView("redirect:/home");
	}

	// HTTP login session initiation
	@RequestMapping(value = "/userlogin", method = RequestMethod.POST)
	public ResponseEntity<List<UserInfo>> userLogin(@Valid @RequestBody UserInfo user, HttpSession sessionObj) {
		List<UserInfo> success = userService.verifyPassword(user.getUserName(), user.getPassword());
		String isValid = user.getPassword();
		if (!(success.get(0).getPassword().equals(isValid))) {
			sessionObj.setAttribute("error", "Username or password invalid!");
			return new ResponseEntity<List<UserInfo>>(HttpStatus.CONFLICT);
		} else {
			sessionObj.setAttribute("user", success.get(0));
			return new ResponseEntity<List<UserInfo>>(success, HttpStatus.OK);
		}
	}
	
	// HTTP session logout
	@RequestMapping(value = "/userlogout", method = RequestMethod.POST)
	public ModelAndView logoutUser(HttpSession sessionObj, ModelAndView mv) {
		sessionObj.invalidate();
		return new ModelAndView("redirect:/");
	}

	// PUT one user to user table
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public ModelAndView updateUser(@ModelAttribute UserInfo user, int userId, HttpSession sessionObj, ModelAndView mv) {
		mv.addObject("user", this.userService.getUserById(userId));
		userService.updateUser(user);
		sessionObj.setAttribute("user", user);
		return new ModelAndView("/updateUser");
	}
	
	// DELETE one user to user table
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public ModelAndView deleteUser(@ModelAttribute UserInfo user, int userId, HttpSession sessionObj, ModelAndView mv) {
		mv.addObject("user", this.userService.getUserById(userId));
		userService.deleteUser(userId);
		sessionObj.invalidate();
		mv.setViewName("redirect:/");
		return mv;
	}

	// POST one review to review table
	@RequestMapping(value = "/addReview", method = RequestMethod.POST)
	public ModelAndView eachReview(@ModelAttribute ReviewInfo review) {
		reviewService.addReview(review);
		return new ModelAndView("redirect:/home");
	}

	// GET all reviews by one beerName from review table HTTP SESSION
	@RequestMapping(value = "/beerDetails", params = {"beerInfo"} )
	public ModelAndView getReviewsByBeerName(@RequestParam("beerInfo") String beerInfo, ModelAndView mv) {
		mv.addObject("reviews", this.reviewService.getReviewByBeerName(beerInfo));
		mv.setViewName("beerDetails");
		return mv;
	}
	
	// GET all reviews by one beerName from review table HTTP SESSION
	@RequestMapping(value = "/yourBeer", params = {"beerInfo"} )
	public ModelAndView getAllReviewsByBeerName(@RequestParam("beerInfo") String beerInfo, ModelAndView mv) {
		mv.addObject("reviews", this.reviewService.getReviewByBeerName(beerInfo));
		mv.setViewName("yourBeer");
		return mv;
	}
	
	// GET all reviews by one userName from review table
	@RequestMapping(value = "/updateUser", method = RequestMethod.GET)
	public ModelAndView updateUserView(HttpSession sessionObj, ModelAndView mv) {
		UserInfo user = (UserInfo) sessionObj.getAttribute("user");
		mv.addObject("reviews", this.reviewService.getReviewsByUserName(user.getUserName()));
		mv.setViewName("updateUser");
		return mv;
	}
	
	// DELETE one review to review table	
	@RequestMapping(value = "/deleteReview", method = RequestMethod.POST)
	public ModelAndView deleteReview(@ModelAttribute ReviewInfo review, int reviewId, ModelAndView mv) {
		mv.addObject("reviews", this.reviewService.getReviewById(reviewId));
		reviewService.deleteReview(reviewId);
		mv.setViewName("redirect:/updateUser");
		return mv;
	}
}
