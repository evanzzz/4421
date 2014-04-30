//package com.springapp.mvc;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//import java.util.Properties;
//
///**
// * Created by ZWH on 4/20/2014.
// */
//@Controller
//public class ClinicController {
//
//    private final Clinic clinic;
//
//    @Autowired
//    public ClinicController(Clinic clinic) {
//        this.clinic = clinic;
//    }
//
//    @RequestMapping("/")
//    public void welcomeHandler() {
//    }
//
//    @RequestMapping("/vets")
//    public ModelMap vetsHandler() {
//        return new ModelMap(this.clinic.getVets());
//    }
//
//
//    @RequestMapping(value="/owners/{ownerId}", method= RequestMethod.GET)
//    public String findOwner(@PathVariable("ownerId") String theOwner, Model model) {
//        // implementation omitted
//    }
//    @RequestMapping(value="/owners/{ownerId}/pets/{petId}", method=RequestMethod.GET)
//    public String findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) {
//        Owner owner = ownerService.findOwner(ownerId);
//        Pet pet = owner.getPet(petId);
//        model.addAttribute("pet", pet);
//        return "displayPet";
//    }
//
//    @RequestMapping("/spring-web/{symbolicName:[a-z-]}-{version:\\d\\.\\d\\.\\d}{extension:\\.[a-z]}")
//    public void handle(@PathVariable String version, @PathVariable String extension) {
//        // ...
//    }
//
//
//
//    // GET /pets/42;q=11;r=22
//
//    @RequestMapping(value = "/pets/{petId}", method = RequestMethod.GET)
//    public void findPet(@PathVariable String petId, @MatrixVariable int q) {
//
//        // petId == 42
//        // q == 11
//
//    }
//
//    // GET /owners/42;q=11/pets/21;q=22
//
//    @RequestMapping(value = "/owners/{ownerId}/pets/{petId}", method = RequestMethod.GET)
//    public void findPet(
//            @MatrixVariable(value="q", pathVar="ownerId") int q1,
//            @MatrixVariable(value="q", pathVar="petId") int q2) {
//
//        // q1 == 11
//        // q2 == 22
//
//    }
//
//    // GET /pets/42
//
//    @RequestMapping(value = "/pets/{petId}", method = RequestMethod.GET)
//    public void findPet(@MatrixVariable(required=false, defaultValue="1") int q) {
//
//        // q == 1
//
//    }
//
//// GET /owners/42;q=11;r=12/pets/21;q=22;s=23
//
//    @RequestMapping(value = "/owners/{ownerId}/pets/{petId}", method = RequestMethod.GET)
//    public void findPet(
//            @MatrixVariable Map<String, String> matrixVars,
//            @MatrixVariable(pathVar="petId") Map<String, String> petMatrixVars) {
//
//            // matrixVars: ["q" : [11,22], "r" : 12, "s" : 23]
//            // petMatrixVars: ["q" : 11, "s" : 23]
//
//}
//
////Note that to enable the use of matrix variables, you must set the removeSemicolonContent property of RequestMappingHandlerMapping to false. By default it is set to true.
//
//    @Controller
//    @RequestMapping("/owners/{ownerId}")
//    public class RelativePathUriTemplateController {
//
//        @RequestMapping(value = "/pets/{petId}", method = RequestMethod.GET,
//                params="myParam=myValue", headers="myHeader=myValue")
//        public void findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) {
//            // implementation omitted
//        }
//
//    }
//
//    //The following is an example of a form controller from the PetPortal sample application using this annotation:
//    @Controller
//    @RequestMapping("EDIT")
//    @SessionAttributes("site")
//    public class PetSitesEditController {
//
//        private Properties petSites;
//
//        public void setPetSites(Properties petSites) {
//            this.petSites = petSites;
//        }
//
//        @ModelAttribute("petSites")
//        public Properties getPetSites() {
//            return this.petSites;
//        }
//
//        @RequestMapping // default (action=list)
//        public String showPetSites() {
//            return "petSitesEdit";
//        }
//
//        @RequestMapping(params = "action=add") // render phase
//        public String showSiteForm(Model model) {
//            // Used for the initial form as well as for redisplaying with errors.
//            if (!model.containsAttribute("site")) {
//                model.addAttribute("site", new PetSite());
//            }
//
//            return "petSitesAdd";
//        }
//
//        @RequestMapping(params = "action=add") // action phase
//        public void populateSite(@ModelAttribute("site") PetSite petSite,
//                                 BindingResult result, SessionStatus status, ActionResponse response) {
//            new PetSiteValidator().validate(petSite, result);
//            if (!result.hasErrors()) {
//                this.petSites.put(petSite.getName(), petSite.getUrl());
//                status.setComplete();
//                response.setRenderParameter("action", "list");
//            }
//        }
//
//        @RequestMapping(params = "action=delete")
//        public void removeSite(@RequestParam("site") String site, ActionResponse response) {
//            this.petSites.remove(site);
//            response.setRenderParameter("action", "list");
//        }
//    }
//
//
//}
//
//
