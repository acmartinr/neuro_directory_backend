package com.example.demo.controller;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.utils.FileUploadUtil;
import com.mongodb.client.model.Sorts;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.assertions.Assertions.assertFalse;
import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
@CrossOrigin
@Controller
//@RestController
public class HelloController {
    @Autowired
    ItemRepository groceryItemRepo;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    FeedBackrepository feedBackrepository;
    @Autowired
    CustomItemRepository customRepo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BookingRepository bookingRepository;

    @GetMapping("/")
    public String index() {
        // placeRepository.save(new Place(999999999999L,"(-12.2,22.22)","Place for fun weekend",20.0f,"Sarao Bar Playa","La Habana","19 entre D y E ","La Habana",100,0,true,"bar"));
        return "Greetings from Spring Boot!";
    }
    @GetMapping("/admin")
    public String greeting( Model model) {
        List<Place> lp = placeRepository.findAll();
        model.addAttribute("name", "Martin");
        model.addAttribute("places", lp);
        return "greeting";
    }
    @GetMapping("/bookinglist")
    public String bookings( Model model) {
        List<Booking> bl = bookingRepository.findAll();
        model.addAttribute("bookings", bl);
        return "bookings";
    }
    @GetMapping("/admin/events/{placeId}")
    public String events(@PathVariable String placeId, Model model) {
        List<Event> le = eventRepository.findAll(placeId);
        model.addAttribute("name", "Martin");
        model.addAttribute("events", le);
        return "events";
    }

    @GetMapping("/admin/addBusiness")
    public String addBusiness(Model model) {
        model.addAttribute("newPlace", new Place());
        return "addBusiness";
    }
    @GetMapping("/admin/addEvent")
    public String addEvent(Model model) {
        model.addAttribute("newEvent", new Event());
        return "addEvent";
    }

    @GetMapping("/grocery")
    public GroceryItem grocery() {
        GroceryItem item = groceryItemRepo.findItemByName("Whole Wheat Biscuit");
        return item;
    }
    @GetMapping("/place")
    public @ResponseBody Place place() {
        Place place = placeRepository.findPlaceByName("Sarao Bar Playa");
        //List<Place> list = placeRepository.findAll("bar");
        return place;
    }

    @PostMapping("/support/sendfeedback")
    public @ResponseBody String sendFeedBack(@RequestBody Feedback feedbacktxt){
        String uniqueID = UUID.randomUUID().toString();
        try {
           // Feedback fb = new Feedback(uniqueID,feedbacktxt);
            feedBackrepository.save(feedbacktxt);
            return "200";
        }catch (Exception e){
            e.printStackTrace();
            return "400";
        }
    }

    @PostMapping("/admin/addBusiness")
    //public RedirectView addPlace(@ModelAttribute Place newPlace, @RequestParam("image") MultipartFile multipartFile,@RequestParam("image_icon") MultipartFile multipartFile2) throws IOException {
    public RedirectView addPlace(@RequestPart("newPlace") Place newPlace, @RequestPart("image") MultipartFile multipartFile,@RequestPart("image_icon") MultipartFile multipartFile2) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uniqueID = UUID.randomUUID().toString();

        newPlace.setId(uniqueID);
        newPlace.setPhotos(fileName);
        newPlace.setIconPhotos("icon_image_"+fileName);
        newPlace.setEvents(Arrays.asList(new Event[] {}));

        Place place = placeRepository.save(newPlace);
        String uploadDir = "user-photos/" + place.getId();
        String uploadDir2 = "user-photos/" + "icon_image_"+place.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        FileUploadUtil.saveFile(uploadDir2, fileName, multipartFile2);

        //  System.out.println( newPlace.getLocation());
        return new RedirectView("/admin", true);
    }


    @PutMapping("/admin/addBusiness")
    //public RedirectView addPlace(@ModelAttribute Place newPlace, @RequestParam("image") MultipartFile multipartFile,@RequestParam("image_icon") MultipartFile multipartFile2) throws IOException {
    public @ResponseBody Place updatePlace(@RequestPart("newPlace") Place newPlace, @RequestPart("image") MultipartFile multipartFile,@RequestPart("image_icon") MultipartFile multipartFile2) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uniqueID = newPlace.getId();
        Optional<Place> entity=placeRepository.findById(uniqueID);
        List<Event> events = entity.get().getEvents();

        if(fileName.equals("no_image.jpg")){
            newPlace.setPhotos(entity.get().getPhotos());
            newPlace.setIconPhotos(entity.get().getIconPhotos());
        }else{
            newPlace.setPhotos(fileName);
            newPlace.setIconPhotos("icon_image_"+fileName);
            String uploadDir = "user-photos/" + newPlace.getId();
            String uploadDir2 = "user-photos/" + "icon_image_"+newPlace.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            FileUploadUtil.saveFile(uploadDir2, fileName, multipartFile2);
        }
        System.out.println(multipartFile.getOriginalFilename());

        newPlace.setEvents(events);
        placeRepository.save(newPlace);
        return newPlace;
    }
    @PostMapping("/admin/addEvent")
    public RedirectView addEvent(@ModelAttribute Event newEvent, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uniqueID = UUID.randomUUID().toString();

        newEvent.setId(uniqueID);
        newEvent.setPhotos(fileName);

        Event event = eventRepository.save(newEvent);
        System.out.println(newEvent.getDate());
        Update update = new Update();
        update.push("events", newEvent);
        BasicQuery query = new BasicQuery("{id:'"+newEvent.getPlaceId()+"'}");
        mongoTemplate.updateFirst(query, update, Place.class, "Place");

        String uploadDir = "user-photos/" + event.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        //  System.out.println( newPlace.getLocation());
        return new RedirectView("/admin/events/"+newEvent.getPlaceId(), true);
    }
    /*
    @PostMapping("/place")
    public @ResponseBody Place addPlace(@RequestBody Place newPlace) {
        newPlace.setEvents(Arrays.asList(new Event[] {}));
      //  System.out.println( newPlace.getLocation());
        return placeRepository.save(newPlace);
    }

     */



    @GetMapping("/allevents/{placeId}")
    public  List<Event> addEvent(@PathVariable String placeId) {
     //   List<Event> list = eventRepository.findAll(Sort.by(Sort.Direction.DESC,"strDate"));
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC,"cover"));
        List<Event> list = mongoTemplate.find(query,Event.class);
        for(Event ls:list){
            System.out.println(ls.getCover());
        }
        return list;
    }


    @GetMapping("/allevents")
    public  @ResponseBody List<Event> allEvents() {
        //   List<Event> list = eventRepository.findAll(Sort.by(Sort.Direction.DESC,"strDate"));
        //14400000 = 4Hrs
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC,"cover"));
        query.addCriteria(Criteria.where("date").gt(timestamp.getTime() - 14400000));
        List<Event> list = mongoTemplate.find(query,Event.class);
        /*
        for(Event ls:list){
            System.out.println(ls.getCover());
        }

         */
        return list;
    }

    @GetMapping("/admin/allevents")
    public  @ResponseBody List<Event> adminAllEvents() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC,"cover"));
        List<Event> list = mongoTemplate.find(query,Event.class);
        return list;
    }

    @GetMapping("/places/{placeName}")
    public @ResponseBody List<Place> filterPlace(@PathVariable String placeName) {
        BasicQuery query =
                new BasicQuery("{\"dbbusinessName\": {$regex : '^" + placeName + "','$options' : 'i'} }");
        query.collation(Collation.of("en").
                strength(Collation.ComparisonLevel.secondary()));
        query.limit(10);
        List<Place> list = mongoTemplate.find(query,Place.class);
        return list;
    }

    @GetMapping("/allplaces/{type}")
    public @ResponseBody List<Place> allplaces(@PathVariable String type) {
        List<Place> list;
        if(!type.equals("all")){
            list = placeRepository.findAll(type);
        }else{
            list = placeRepository.findAll();
        }

        return list;
    }

    @GetMapping("/near_place/{lat}/{longt}/{maxDistance}")
    public @ResponseBody List<Place> searchNearLocation(@PathVariable String lat,@PathVariable String longt,@PathVariable String maxDistance) {
        List<Place> list = searchbylocation(lat,longt,maxDistance);
        if(!list.isEmpty()){
            List<Event> newEvents = new ArrayList<>();
            List<Event> le = list.get(0).getEvents();
            for(Event e: le){
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if(e.getDate() > (timestamp.getTime() - 14400000)){
                    newEvents.add(e);
                }
            }
            list.get(0).setEvents(newEvents);
        }
        return list;
    }
    @GetMapping("/near_place/{lat}/{longt}/{maxDistance}/{category}")
    public @ResponseBody List<Place> searchNearLocationbyCategory(@PathVariable String lat,@PathVariable String longt,@PathVariable String maxDistance,@PathVariable String category) {
        List<Place> list = searchbylocationbyCategory(lat,longt,maxDistance,category);
        return list;
    }
    public List<Place> searchbylocation(String lat, String longt, String maxDistance) {
        //BasicQuery query1 = new BasicQuery("{geoLocation:{ $near: { $geometry: { type: 'Point',location: ["+ lat+","+ longt+" ] }, $minDistance: 10, $maxDistance: "+maxDistance+"}}}");
        BasicQuery query1 = new BasicQuery("{location:{ $near: ["+ lat+","+ longt+" ] ,$maxDistance: "+maxDistance+"}}}");
        List<Place> venues1 = mongoTemplate.find(query1, Place.class);
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"strDate"));
        for(int i = 0;i<venues1.size();i++){
            //order each event by date
        }
       // List<Event> listEvents = mongoTemplate.find(query,Event.class);
       // venues1.get(0).setEvents(listEvents);
        //Lo anterior esta mal ya que inserta los eventos de un place al primero que aparece
        return venues1;
    }
    public List<Place> searchbylocationbyCategory(String lat, String longt, String maxDistance,String category) {
        //BasicQuery query1 = new BasicQuery("{geoLocation:{ $near: { $geometry: { type: 'Point',location: ["+ lat+","+ longt+" ] }, $minDistance: 10, $maxDistance: "+maxDistance+"}}}");
        BasicQuery query1 = new BasicQuery("{location:{ $near: ["+ lat+","+ longt+" ] ,$maxDistance: "+maxDistance+"}}}");
        List<Place> venues1 = mongoTemplate.find(query1, Place.class);
        venues1 = venues1.stream().filter(f -> category.toLowerCase(Locale.ROOT).equals(f.getCategory())).collect(Collectors.toList());
        System.out.println("ddd");
        System.out.println(category);
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"strDate"));
        for(int i = 0;i<venues1.size();i++){
            //order each event by date
        }
        // List<Event> listEvents = mongoTemplate.find(query,Event.class);
        // venues1.get(0).setEvents(listEvents);
        //Lo anterior esta mal ya que inserta los eventos de un place al primero que aparece
        return venues1;
    }


    //CREATE
    void createGroceryItems() {
        System.out.println("Data creation started...");
        groceryItemRepo.save(new GroceryItem("Whole Wheat Biscuit", "Whole Wheat Biscuit", 5, "snacks"));
        groceryItemRepo.save(new GroceryItem("Kodo Millet", "XYZ Kodo Millet healthy", 2, "millets"));
        groceryItemRepo.save(new GroceryItem("Dried Red Chilli", "Dried Whole Red Chilli", 2, "spices"));
        groceryItemRepo.save(new GroceryItem("Pearl Millet", "Healthy Pearl Millet", 1, "millets"));
        groceryItemRepo.save(new GroceryItem("Cheese Crackers", "Bonny Cheese Crackers Plain", 6, "snacks"));
        System.out.println("Data creation complete...");
    }
    // READ
    // 1. Show all the data
    public void showAllGroceryItems() {

        groceryItemRepo.findAll().forEach(item -> System.out.println(getItemDetails(item)));
    }

    // 2. Get item by name
    public void getGroceryItemByName(String name) {
        System.out.println("Getting item by name: " + name);
        GroceryItem item = groceryItemRepo.findItemByName(name);
        System.out.println(getItemDetails(item));
    }

    // 3. Get name and quantity of a all items of a particular category
    public void getItemsByCategory(String category) {
        System.out.println("Getting items for the category " + category);
        List<GroceryItem> list = groceryItemRepo.findAll(category);

        list.forEach(item -> System.out.println("Name: " + item.getName() + ", Quantity: " + item.getQuantity()));
    }

    // 4. Get count of documents in the collection
    public void findCountOfGroceryItems() {
        long count = groceryItemRepo.count();
        System.out.println("Number of documents in the collection: " + count);
    }
    // Print details in readable form

    public String getItemDetails(GroceryItem item) {

        System.out.println(
                "Item Name: " + item.getName() +
                        ", \nQuantity: " + item.getQuantity() +
                        ", \nItem Category: " + item.getCategory()
        );

        return "";
    }


    public void updateCategoryName(String category) {

        // Change to this new value
        String newCategory = "munchies";

        // Find all the items with the category snacks
        List<GroceryItem> list = groceryItemRepo.findAll(category);

        list.forEach(item -> {
            // Update the category in each document
            item.setCategory(newCategory);
        });

        // Save all the items in database
        List<GroceryItem> itemsUpdated = groceryItemRepo.saveAll(list);

        if(itemsUpdated != null)
            System.out.println("Successfully updated " + itemsUpdated.size() + " items.");
    }
    // DELETE
    public void deleteGroceryItem(String id) {
        groceryItemRepo.deleteById(id);
        System.out.println("Item with id " + id + " deleted...");
    }


    @GetMapping("/event/{eventid}")
    public @ResponseBody Event getEvent(@PathVariable String eventid) {
        return eventRepository.findById(eventid).get();
    }


    @DeleteMapping("/deleteplace/{placeId}")
    public @ResponseBody String deletePlace(@PathVariable String placeId) {
        try{
            Place p = placeRepository.findById(placeId).get();
            if(p.getEvents() != null){
                List<Event> le = p.getEvents();
                for(Event e:le){
                    eventRepository.deleteById(e.getId());
                }
            }

            placeRepository.deleteById(placeId);
            return "OK";
        }catch (Exception e){
            e.printStackTrace();
            return "BAD";
        }
    }


    // UPDATE
    public void updateItemQuantity(String name, float newQuantity) {
        System.out.println("Updating quantity for " + name);
        customRepo.updateItemQuantity(name, newQuantity);
    }
}
