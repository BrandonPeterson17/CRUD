package com.MusicOrganizer;

import com.MusicOrganizer.Entities.AlbumEntity;
import com.MusicOrganizer.Entities.ArtistEntity;
import com.MusicOrganizer.Entities.SongDTO;
import com.MusicOrganizer.Entities.SongEntity;
import com.MusicOrganizer.Repositories.AlbumRepository;
import com.MusicOrganizer.Repositories.ArtistRepository;
import com.MusicOrganizer.Repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 1/11/2017.
 */
@Controller
public class MusicController {

    @Autowired
    SongRepository songRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;

    @RequestMapping(value = "/")
    public String home(ModelMap song) {
        /*ArrayList<SongEntity> songs = (ArrayList<SongEntity>)songRepository.findAll();
        for (int index = 0; index < songs.size(); index++) {
            song.put(String.valueOf(songs.get(index).getId()), songs.get(index));
        }
        songMap = song;*/
        return "forward:/home/?page=0";
    }

    @RequestMapping(value = "/home")
    public String goHome(ModelMap mo, @PageableDefault(value = 5, page = 0, sort = {"id"}) Pageable pageable) {

//        Page<SongEntity> allTheSongs = songRepository.findAll(pageable);
//        mo.put("song", allTheSongs);
        return "home";
    }

    @ModelAttribute("song")
    public Page<SongDTO> findAllSongs(@PageableDefault(value = 5, page = 0, sort = {"id"}) Pageable pageable, ModelMap modelMap) {
//        String s = "s";
//        List<AlbumEntity> albums = albumRepository.findAll();
//        Set<SongEntity> songEntitySet = albums.get(0).getSongEntities();



        List<SongEntity> songs = songRepository.findAll();
        List<SongDTO> songsDTO = new ArrayList<>();
        for(SongEntity songEntity: songs) {
            SongDTO dto = new SongDTO();
            dto.setId(songEntity.getId());
            dto.setTitle(songEntity.getTitle());
            dto.setGenre(songEntity.getGenre());

            AlbumEntity albumEntity = songEntity.getAlbumEntity();
            if(albumEntity == null) {
                dto.setAlbum("(none)");
                dto.setDate("(none)");
                dto.setArtist("(none)");
            } else {
                dto.setAlbum(albumEntity.getTitle());
                dto.setDate(albumEntity.getDate());

                ArtistEntity artistEntity = albumEntity.getArtistEntity();
                if(artistEntity == null)
                    dto.setArtist("(none)");
                else
                    dto.setArtist(artistEntity.getArtist());
            }
            songsDTO.add(dto);
        }
        Page<SongDTO> page = new PageImpl<SongDTO>(songsDTO, pageable, songsDTO.size());
        return page;

//        modelMap.put("AllSongs", songRepository.findAll(new Sort(Sort.Direction.ASC, "title")));
//        Page<SongEntity> allTheSongs = songRepository.findAll(pageable);
//        return allTheSongs;
    }
//    @RequestParam(value = "title") String title,

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String findByTitle(ModelMap modelMap, @RequestParam("page") String page,
                              @ModelAttribute("songForm") Song songForm) {
        String title = songForm.getTitle();
        Pageable pageable = new PageRequest(Integer.parseInt(page), 5, new Sort(Sort.Direction.ASC, "title"));
        Page<SongEntity> someOfTheSongs = songRepository.findByTitle(pageable, "%" + title + "%");
        modelMap.put("searchSong", someOfTheSongs);
        //mo.put("title", title);
        return "search";
    }

    @RequestMapping(value = "/search/page", method = RequestMethod.POST)
    public String turnSearchPage(ModelMap modelMap, @RequestParam("page") String page,
                                 @RequestParam("title") String title) {
        Song songForm = new Song();
        songForm.setTitle(title);
        modelMap.put("songForm", songForm);
        Pageable pageable = new PageRequest(Integer.parseInt(page), 5, new Sort(Sort.Direction.ASC, "title"));
        Page<SongEntity> someOfTheSongs = songRepository.findByTitle(pageable, "%" + title + "%");
        modelMap.put("searchSong", someOfTheSongs);
        return "search";
    }

    @RequestMapping(value = "/search/page", method = RequestMethod.GET)
    public String loadSearchPage(ModelMap modelMap, @RequestParam("page") String page,
                                 @RequestParam("title") String title) {
        Song songForm = new Song();
        songForm.setTitle(title);
        modelMap.put("songForm", songForm);
        Pageable pageable = new PageRequest(0, 5, new Sort(Sort.Direction.ASC, "title"));
        Page<SongEntity> someOfTheSongs = songRepository.findByTitle(pageable, "%" + title + "%");
        modelMap.put("searchSong", someOfTheSongs);
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String loadSearchPage(ModelMap modelMap) {
        Song songForm = new Song();
        modelMap.put("songForm", songForm);
        return "search";
    }

    //    @RequestMapping(value = "/search/action", method = RequestMethod.POST)
//    public String search(@RequestParam String title, @PageableDefault(value = 5, page = 0, sort = {"id"})Pageable pageable, ModelMap model) {
//        //Page<SongEntity> p = model.get("song");
//
//
//        List<SongEntity> songs = songRepository.findAll();
//        for (int index = 0; index < songs.size(); index++)
//            if(!songs.get(index).getTitle().toLowerCase().contains(title.toLowerCase()))
//                songs.remove(index--);
//        //cat.put("songs", songs);
//        for (SongEntity s: songs) {
//            System.out.println(s.getTitle() + " - " + s.getArtist());
//        }
//        Page<SongEntity> page = new PageImpl<SongEntity>(songs, pageable, songs.size());
//        model.addAttribute("songPage", page);
//        return "search";
//    }
//              OLD WAY
//    @RequestMapping(value = "/home")
//    public String goHome(ModelMap mo, @RequestParam("page") int pageNum) {
//        //Pageable pageable = new PageRequest(pageNum, 5, new Sort(Sort.Direction.ASC, "id"));
//        AbstractPageRequest songs = new PageRequest(pageNum, 5, new Sort(Sort.Direction.ASC, "id"));
//        Page<SongEntity> allTheSongs = songRepository.findAll(songs);
//        mo.put("song", allTheSongs);
//        return "home";
//    }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @RequestMapping(value = "/email/{id}")
    public String email(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.put("emailSong", songRepository.findById(Integer.toUnsignedLong(Integer.parseInt(id))).get(0));
        //modelMap.put("emailSong", ((ArrayList) modelMap.get("AllSongs")).get(Integer.parseInt(id)));
        return "email";
    }

    @RequestMapping(value = "/send/{id}")
    public String sendEmail(ModelMap modelMap, @PathVariable("id") String id, @RequestParam("emailIn") String email) {
        MusicMailSender sender = new MusicMailSender();
        sender.setMailSender();
        sender.setTemplateMessage(new SimpleMailMessage());
        String[] recipients = {email};
        String[] songs = {songRepository.findById(Integer.toUnsignedLong(Integer.parseInt(id))).get(0).getString()};
        sender.sendMail(MusicMailSender.TYPE_JAVA, recipients, songs, modelMap);
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("musicorganizer/src/main/webapp/WEB-INF/config/servlet-config.xml");
//        MusicMailSender ms = (MusicMailSender) context.getBean("mailSender");
//        ms.sendMail();
        return "redirect:/home/?page=0";
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap modelMap) {
        Song songForm = new Song();
        modelMap.put("songForm", songForm);
        return "add-artist";
    }

    /*@RequestMapping(value = "/search")
    public String search() {
        return "search";
    }*/

    @RequestMapping(value = "/add-error")
    public String add_error() {
        return "add-error";
    }


//    @ModelAttribute("song") //debug
//    public Page<SongEntity> data(@PageableDefault(value = 5, page = 0, sort = {"id"})Pageable pageable, ModelMap model) {
//        AbstractPageRequest songs = new PageRequest(homePageNum, 5, new Sort(Sort.Direction.ASC, "id"));
//        Page<SongEntity> allTheSongs = songRepository.findAll(songs);
//        return allTheSongs;
//    }


//    @ModelAttribute("searchSong")
//    public Page<SongEntity> searchPage(@RequestParam String title, @PageableDefault(value = 5, page = 0, sort = {"id"})Pageable pageable) {
//        AbstractPageRequest songs = new PageRequest(searchPageNum, 5, new Sort(Sort.Direction.ASC, "id"));
//
//        return songRepository.findByTitleIgnoreCaseOrderByIdAsc(songs, title);
//
//    }


    @RequestMapping(value = "/add/artist", method = RequestMethod.POST)
    public String add(@RequestParam("artist") String artist, @ModelAttribute("songForm") Song song,
                      ModelMap modelMap) {
        ErrorInput errorInput = new ErrorInput(songRepository, albumRepository, artistRepository);
        List<String> errmsg = errorInput.checkArtist(artist);
        if (errmsg.size() > 0) {
            modelMap.put("inputErrors", errmsg);
            song.setArtist(artist);
            return "add-artist";
        }
        artistRepository.save(new ArtistEntity(artist));
        modelMap.put("inputErrors", null);
        return "redirect:/";
    }

    @RequestMapping(value = "/add/album", method = RequestMethod.POST)
    public String addAlbum(@RequestParam("album") String album, @RequestParam("date") String date,
                           @RequestParam("artist") String artist, @ModelAttribute("songForm") Song song,
                           ModelMap modelMap) {
        ErrorInput errorInput = new ErrorInput(songRepository, albumRepository, artistRepository);
        List<String> srrmsg = errorInput.checkAlbum(album, artist);
        return "home";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, @RequestParam("page") int page) {
        songRepository.delete(id);
        return "redirect:/home/?page=" + page;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String edit(@PathVariable("id") Long id, @RequestParam("title") String title,
                       @RequestParam("artist") String artist, @RequestParam("genre") String genre,
                       @RequestParam("rating") String rating, @RequestParam("page") int page,
                       ModelMap modelMap) {
        SongEntity song = songRepository.findOne(id);
        if (title.equals(""))
            title = song.getTitle();
        if (rating == null || rating.isEmpty()) {
            rating = Integer.toString(song.getRating());
            System.out.println("Rating not entered");
        }
        System.out.println("Title: " + title + "\nArtist: " + artist + "\nGenre: " + genre +
                "\nRating: " + rating + "\n");
        ErrorInput errorInput = new ErrorInput(title, artist, genre, rating, songRepository, (int) song.getId());
        if (errorInput.hasErrors()) {
            //modelMap.put("inputErrors", errorInput.getErrorStrings(ErrorInput.CONSOLE));
            modelMap.put("inputErrors", errorInput.getErrorMessages());
            System.out.println("Input has the following errors:\n" + errorInput.getErrorStrings(ErrorInput.PRINT));
        } else {
            song.setTitle(title);
            if (!genre.equals(""))
                song.setGenre(genre);
            song.setRating(Integer.parseInt(rating));
            songRepository.save(song);
            modelMap.put("inputErrors", null);
        }
        return "redirect:/home/?page=" + page;
    }


}
