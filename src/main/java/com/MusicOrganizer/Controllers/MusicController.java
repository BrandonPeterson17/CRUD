package com.MusicOrganizer.Controllers;

import com.MusicOrganizer.Entities.AlbumEntity;
import com.MusicOrganizer.Entities.ArtistEntity;
import com.MusicOrganizer.Entities.SongDTO;
import com.MusicOrganizer.Entities.SongEntity;
import com.MusicOrganizer.ErrorInput;
import com.MusicOrganizer.MusicMailSender;
import com.MusicOrganizer.Repositories.AlbumRepository;
import com.MusicOrganizer.Repositories.ArtistRepository;
import com.MusicOrganizer.Repositories.SongRepository;
import com.MusicOrganizer.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.PageableDefault;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.lang.Math.sqrt;

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
    public String home(ModelMap modelMap) {
        return "forward:/home/?page=0";
    }

    @RequestMapping(value = "/home")
    public String goHome(ModelMap modelMap) {

        Song editForm = new Song();
        modelMap.put("albums", getAlbums());
        modelMap.put("editForm", editForm);
        modelMap.put("pageNum", 0);
        return "home";
    }

//    public boolean testPalindrome(int n) {
//        String s = Integer.toString(n);
//        String first = s.substring(0, s.length()/2);
//        String last = s.substring(s.length() - s.length()/2);
//        for(int i=0; i<first.length(); i++)
//            if(first.charAt(i) != last.charAt(last.length()-1-i))
//                return false;
//        return true;
//    }
//
//    public boolean isPrime(int n) {
//        for (int i = 0; i < sqrt(n); i++) {
//
//        }
//    }

    @ModelAttribute("song")
    public Page<SongEntity> findAllSongs(@PageableDefault(value = 5, page = 0, sort = {"title"}) Pageable pageable, ModelMap modelMap,
                                      @RequestParam(name = "page", required = false, defaultValue = "0")String pageNum) {

        List<SongEntity> allTheSongs = songRepository.findAll(new Sort(Sort.Direction.ASC, "title"));
        List<AlbumEntity> allTheAlbums = albumRepository.findAll(new Sort(Sort.Direction.ASC, "title"));
        List<ArtistEntity> allTheArtists = artistRepository.findAll(new Sort(Sort.Direction.ASC, "artist"));
        modelMap.put("totalPages", (allTheSongs.size()+4)/5 );
        modelMap.put("totalAlbumPages", (albumRepository.findAll().size()+4)/5);
        modelMap.put("totalArtistPages", (artistRepository.findAll().size()+4)/5);
        modelMap.put("artistRepo", artistRepository.findAll());
        modelMap.put("songTry", allTheSongs);
        modelMap.put("albums", allTheAlbums);
        modelMap.put("artists", allTheArtists);
        return null;
    }

    @RequestMapping(value = "/getAlbums", method = RequestMethod.POST)
    @ResponseBody
    public String getAlbums(@RequestParam("songId") String songId, @RequestParam("artistId") String artistId) {
        ArtistEntity artistEntity = artistRepository.findById(Long.parseLong(artistId));
        List<AlbumEntity> albumEntityList = artistEntity.getAlbumEntities();
        List<String> options = new LinkedList<>();
        AlbumEntity albumE = songRepository.findById(Long.parseLong(songId)).getAlbumEntity();
        for (AlbumEntity albumEntity: albumEntityList) {
            if(albumEntity.equals(albumE))
                options.add(0, "<option value=\"" + albumEntity.getId() + "\" length=\"20\">" + albumEntity.getTitle() + "</option>");
            else
                options.add("<option value=\"" + albumEntity.getId() + "\">" + albumEntity.getTitle() + "</option>");
        }
        String opt = "";
        for (String s: options) {
            opt += s;
        }
        return opt;
    }

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
    /*
              OLD WAY
    @RequestMapping(value = "/home")
    public String goHome(ModelMap mo, @RequestParam("page") int pageNum) {
        //Pageable pageable = new PageRequest(pageNum, 5, new Sort(Sort.Direction.ASC, "id"));
        AbstractPageRequest songs = new PageRequest(pageNum, 5, new Sort(Sort.Direction.ASC, "id"));
        Page<SongEntity> allTheSongs = songRepository.findAll(songs);
        mo.put("song", allTheSongs);
        return "home";
    }
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
    @RequestMapping(value = "/email/{id}")
    public String email(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.put("emailSong", songRepository.findById(Integer.toUnsignedLong(Integer.parseInt(id))));
        //modelMap.put("emailSong", ((ArrayList) modelMap.get("AllSongs")).get(Integer.parseInt(id)));
        return "email";
    }

    @RequestMapping(value = "/send/{id}")
    public String sendEmail(ModelMap modelMap, @PathVariable("id") String id, @RequestParam("emailIn") String email) {
        MusicMailSender sender = new MusicMailSender();
        sender.setMailSender();
        sender.setTemplateMessage(new SimpleMailMessage());
        String[] recipients = {email};
        String[] songs = {songRepository.findById(Integer.toUnsignedLong(Integer.parseInt(id))).getString()};
        sender.sendMail(MusicMailSender.TYPE_JAVA, recipients, songs, modelMap);
        return "redirect:/home/?page=0";
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @RequestMapping(value = "/addartist")
    public String create(ModelMap modelMap) {
        Song songForm = new Song();
        modelMap.put("songForm", songForm);
        return "add-artist";
    }

    @RequestMapping(value = "/add-error")
    public String add_error() {
        return "add-error";
    }

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

    @RequestMapping(value = "/addalbum")
    public String renderAddAlbum(ModelMap modelMap) {
        Song songForm = new Song();

        modelMap.put("artists", getArtists());
        modelMap.put("songForm", songForm);
        return "add-album";
    }

    private List<String> getArtists() {
        List<ArtistEntity> artistEntities = artistRepository.findAll();
        List<String> artists = new ArrayList<>();
        for(ArtistEntity artistEntity: artistEntities)
            artists.add(artistEntity.getArtist());
        return artists;
    }

    private List<AlbumEntity> getAlbums() {
        List<AlbumEntity> albumEntities = albumRepository.findAll();
        List<String> albums = new ArrayList<>();
        for(AlbumEntity albumEntity: albumEntities)
            albums.add(albumEntity.getTitle() + " (" + albumEntity.getArtistEntity().getArtist() + ")");
        return albumEntities;
    }

    @RequestMapping(value = "/add/album", method = RequestMethod.POST)
    public String addAlbum(@RequestParam("album") String album, @RequestParam("date") String date,
                           @RequestParam("artist") String artist, @ModelAttribute("songForm") Song song,
                           ModelMap modelMap) {
        ErrorInput errorInput = new ErrorInput(songRepository, albumRepository, artistRepository);
        List<String> errmsg = errorInput.checkAlbum(album, artist);
        if(errmsg.size() > 0) {
            modelMap.put("inputErrors", errmsg);
            song.setAlbum(album);
            song.setArtist(artist);
            song.setDate(date);
            modelMap.put("artists", getArtists());
            modelMap.put("songForm", song);
            return "add-album";
        }
        AlbumEntity albumEntity = new AlbumEntity(album, date);
        ArtistEntity artistEntity = artistRepository.findByArtistIgnoreCase(artist).get(0);
        albumEntity.setArtistId(artistEntity.getId());
        albumRepository.save(albumEntity);
        return "redirect:/";
    }

    @RequestMapping(value = "/addsong")
    public String renderAddSong(ModelMap modelMap) {
        Song songForm = new Song();
        modelMap.put("albums", getAlbums());
        modelMap.put("songForm", songForm);
        return "add-song";
    }

    @RequestMapping(value = "/add/song")
    public String addSong(@RequestParam("title") String songTitle, @RequestParam("genre") String genre,
                          @RequestParam("rating") String rating, @RequestParam("id") String id,
                          @ModelAttribute("songForm") Song song, ModelMap modelMap) {
        ErrorInput errorInput = new ErrorInput(songRepository, albumRepository, artistRepository);
        AlbumEntity albumEntity = albumRepository.findById(Integer.parseInt(id));
        List<String> errmsg = errorInput.checkSong(songTitle, genre, rating, albumEntity.getTitle(), albumEntity.getDate(), albumEntity.getArtistEntity().getArtist());
        if(errmsg.size() > 0) {
            modelMap.put("inputErrors", errmsg);
            song.setId(id);
            song.setTitle(songTitle);
            song.setRating(rating);
            song.setGenre(genre);
            modelMap.put("albums", getAlbums());
            modelMap.put("songForm", song);
            return "add-song";
        }
        SongEntity songEntity = new SongEntity(songTitle, genre, Integer.parseInt(rating));
        songEntity.setAlbumId(albumEntity.getId());
        songRepository.save(songEntity);
        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, @RequestParam("page") int page) {
        songRepository.delete(id);
        return "redirect:/home/?page=" + page;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(@RequestParam("songId") Long songId, @RequestParam("title") String title,
                       @RequestParam("albumId") String albumId, @RequestParam("date") String date,
                       @RequestParam("genre") String genre, @RequestParam("rating") String rating,
                       ModelMap modelMap) {
        SongEntity oldSongEnt = songRepository.findOne(songId);
        AlbumEntity oldAlbumEnt = albumRepository.findById(Long.parseLong(albumId));
        List<SongEntity> albumSongs = oldAlbumEnt.getSongEntities();
        List<String> errs = new ArrayList<>();

        for (SongEntity albumSong : albumSongs)
            if (albumSong.getTitle().toLowerCase().equals(oldSongEnt.getTitle().toLowerCase()))
                errs.add(oldSongEnt.getTitle() + " already exists in " + albumSong.getAlbumEntity().getTitle());

        if(title.equals(""))
            errs.add("Title can't be left blank");

        try {
            int dateInt = Integer.parseInt(date);
            if(dateInt < 0 || dateInt > 2017)
                errs.add("Date must be a valid year (ex. 2005)");
        } catch (NumberFormatException ex) {
            errs.add("Date must be an integer year (ex. 1967)");
        }

        try {
            int ratingInt = Integer.parseInt(rating);
            if(ratingInt < 1 || ratingInt > 5)
                errs.add("Rating must be in the range (1-5)");
        } catch (NumberFormatException ex) {
            errs.add("Rating must be an integer in the range (1-5)");
        }

        String message = "";
        if(errs.size() > 0) {
            for(String s: errs)
                message += "<p class=\"errormsg\">" + s + "</p>";
            return message;
        }

        oldAlbumEnt.setDate(date);
        albumRepository.save(oldAlbumEnt);

        oldSongEnt.setAlbumId(Long.parseLong(albumId));
        oldSongEnt.setAlbumEntity(oldAlbumEnt);
        oldSongEnt.setGenre(genre);
        oldSongEnt.setTitle(title);
        oldSongEnt.setRating(Integer.parseInt(rating));
        songRepository.save(oldSongEnt);
        return "";
    }

    @RequestMapping(value = "/album")
    public String renderAlbumView(ModelMap modelMap) {
        return "album-view";
    }

    @RequestMapping(value = "/deleteAlbum/{id}", method = RequestMethod.POST)
    public String deleteAlbum(@PathVariable("id") Long albumId, @RequestParam("page") String page) {
        AlbumEntity albumEntity = albumRepository.findById(albumId);
        List<SongEntity> songEntities = albumEntity.getSongEntities();
        for(SongEntity songEntity: songEntities) {
            songRepository.delete(songEntity.getId());
        }
        albumRepository.delete(albumEntity.getId());
        return "redirect:/album/?page=" + page;
    }

    @RequestMapping(value = "/editAlbum", method = RequestMethod.POST)
    @ResponseBody
    public String saveAlbum(@RequestParam("albumId") Long albumId, @RequestParam("title") String title,
                            @RequestParam("artistId") Long artistId, @RequestParam("date") String date) {
        AlbumEntity oldAlbumEnt = albumRepository.findById(albumId);
        List<String> errs = new ArrayList<>();

        for (AlbumEntity album : artistRepository.findById(artistId).getAlbumEntities())
            if (album.getTitle().toLowerCase().equals(title.toLowerCase())) {
                errs.add(oldAlbumEnt.getTitle() + " already exists by " + album.getArtistEntity().getArtist());
            }

        if(title.equals("")) {
            errs.add("Title can't be left blank");
        }

        try {
            int dateInt = Integer.parseInt(date);
            if(dateInt < 0 || dateInt > 2017)
                errs.add("Date must be a valid year in range (1 - 2017)");
        } catch (NumberFormatException ex) {
            errs.add("Date must be an integer year (ex. 1967)");
        }

        String message = "";
        if(errs.size() > 0) {
            for(String s: errs)
                message += "<p class=\"errormsg\">" + s + "</p>";
            return message;
        }

        oldAlbumEnt.setDate(date);
        oldAlbumEnt.setTitle(title);
        oldAlbumEnt.setArtistId(artistId);
        albumRepository.save(oldAlbumEnt);
        return "";
    }


    @RequestMapping(value = "/artist")
    public String renderArtistView(ModelMap modelMap) {
        return "artist-view";
    }

    @RequestMapping(value = "/deleteArtist/{id}", method = RequestMethod.POST)
    public String deleteArtist(@PathVariable("id") Long artistId, @RequestParam("page") String page) {
        ArtistEntity artistEntity = artistRepository.findOne(artistId);
        for(AlbumEntity albumEntity: artistEntity.getAlbumEntities()) {
            List<SongEntity> songEntities = albumEntity.getSongEntities();
            for (SongEntity songEntity : songEntities) {
                songRepository.delete(songEntity.getId());
            }
            albumRepository.delete(albumEntity.getId());
        }
        artistRepository.delete(artistId);
        return "redirect:/artist/?page=" + page;
    }

    @RequestMapping(value = "/editArtist", method = RequestMethod.POST)
    @ResponseBody
    public String saveArtist(@RequestParam("name") String name, @RequestParam("artistId") Long artistId) {
        ArtistEntity oldArtistEnt = artistRepository.findById(artistId);
        List<String> errs = new ArrayList<>();

        for (ArtistEntity artist : artistRepository.findAll())
            if (artist.getArtist().toLowerCase().equals(name.toLowerCase())) {
                errs.add(name + " already exists.");
            }

        if(name.equals("")) {
            errs.add("Name can't be left blank");
        }

        String message = "";
        if(errs.size() > 0) {
            for(String s: errs)
                message += "<p class=\"errormsg\">" + s + "</p>";
            return message;
        }

        oldArtistEnt.setArtist(name);
        artistRepository.save(oldArtistEnt);
        return "";
    }
}