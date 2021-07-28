package ca.lambton.Wildemo.Controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.maven.model.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//@Controller
//public class ErrorController {
	
	@Controller
	public class MyErrorController implements ErrorController  {

	    @RequestMapping("/error")
	    public String handleError(HttpServletRequest request, ModelMap model) {
	    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    	String errorMsg = "";
	    	String errorCode ="";
	        if (status != null) {
	            Integer statusCode = Integer.valueOf(status.toString());
	        
	            switch ( statusCode) {
	            case 400: {
	                errorMsg = "Bad Request";
	                errorCode = "400";
	                break;
	            }
	            case 401: {
	                errorMsg = "Unauthorized";
	                errorCode = "401";
	                break;
	            }
	            case 404: {
	                errorMsg = "Resource not found";
	                errorCode = "404";
	                break;
	            }
	            case 500: {
	                errorMsg = "Internal Server Error";
	                errorCode = "500";
	                break;
	            }
	        }
	            
//	            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//	            	errMsg =  "Http Error Code: 400. Bad Request";
//	            }
//	            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//	            	errMsg =  "error-500";
//	            }
	        }
	        model.addAttribute("errorMsg", errorMsg);
	        model.addAttribute("errorCode", errorCode);
	        return "error";
	    }
	    
		@Value("${server.error.path}")
		private String errPath;
//	}
//
//    @RequestMapping(value = "errors", method = RequestMethod.GET)
//    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
//        
//        ModelAndView errorPage = new ModelAndView("error");
//        String errorMsg = "";
//        int httpErrorCode = getErrorCode(httpRequest);
//        System.out.println(httpRequest);
//        switch (httpErrorCode) {
//            case 400: {
//                errorMsg = "Http Error Code: 400. Bad Request";
//                break;
//            }
//            case 401: {
//                errorMsg = "Http Error Code: 401. Unauthorized";
//                break;
//            }
//            case 404: {
//                errorMsg = "Http Error Code: 404. Resource not found";
//                break;
//            }
//            case 500: {
//                errorMsg = "Http Error Code: 500. Internal Server Error";
//                break;
//            }
//        }
//        errorPage.addObject("errorMsg", errorMsg);
//        
//        return errorPage;
//    }
//    
//    private int getErrorCode(HttpServletRequest httpRequest) {
//        return (Integer) httpRequest
//          .getAttribute("javax.servlet.error.status_code");
//    }

		@Override
		public String getErrorPath() {
			// TODO Auto-generated method stub
			return errPath;
		}
}
