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
import org.springframework.data.web.PageableDefault;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

        Song editForm = new Song();
        mo.put("albums", getAlbums());
        mo.put("editForm", editForm);
        mo.put("pageNum", 0);
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
    public Page<SongDTO> findAllSongs(@PageableDefault(value = 5, page = 0, sort = {"id"}) Pageable pageable, ModelMap modelMap,
                                      @RequestParam(name = "page", required = false, defaultValue = "0")String pageNum) {
//        String s = "s";
//        List<AlbumEntity> albums = albumRepository.findAll();
//        Set<SongEntity> songEntitySet = albums.get(0).getSongEntities();

//        int n;
//        int k = 2;
//        for (int i = 0; i < 10000; i++) {
//
//        }
//
//
//        modelMap.put("projectEuler", n);









        List<SongEntity> songs = songRepository.findAll();
        List<SongDTO> songsDTO = new ArrayList<>();
        for(SongEntity songEntity: songs) {
            SongDTO dto = new SongDTO();
            dto.setId(songEntity.getId());
            dto.setTitle(songEntity.getTitle());
            dto.setGenre(songEntity.getGenre());
            dto.setRating(songEntity.getRating());

            AlbumEntity albumEntity = songEntity.getAlbumEntity();
            if(albumEntity == null) {
                dto.setAlbum("(none)");
                dto.setDate("(none)");
                dto.setArtist("(none)");
            } else {
                dto.setAlbum(albumEntity.getTitle());
                dto.setDate(albumEntity.getDate());
                dto.setAlbumEntity(albumEntity);
                dto.setAlbumId(Long.toString(albumEntity.getId()));

                ArtistEntity artistEntity = albumEntity.getArtistEntity();
                if(artistEntity == null)
                    dto.setArtist("(none)");
                else {
                    dto.setArtist(artistEntity.getArtist());
                    List<AlbumEntity> albumEntityList = artistEntity.getAlbumEntities();
                    AlbumEntity[] albumEntityArray = new AlbumEntity[albumEntityList.size()];
                    for (int i = 0; i < albumEntityList.size(); i++) {
                        albumEntityArray[i] = albumEntityList.get(i);
                    }
                    dto.setAlbumEntities(albumEntityArray);
                }
            }
            songsDTO.add(dto);
        }
        try {
            Pageable pageRequest = new PageRequest(Integer.parseInt(pageNum), 5, new Sort(Sort.Direction.ASC, "id"));
            Page<SongDTO> page = new PageImpl<>(songsDTO, pageRequest, songsDTO.size());
            modelMap.putIfAbsent("totalPages", page.getTotalPages());
            modelMap.put("artistRepo", artistRepository.findAll());
            return page;
        } catch (NumberFormatException ex) {
            System.out.println("something went horribly wrong");
            return null;
        }

//        modelMap.put("AllSongs", songRepository.findAll(new Sort(Sort.Direction.ASC, "title")));
//        Page<SongEntity> allTheSongs = songRepository.findAll(pageable);
//        return allTheSongs;
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
                options.add(0, "<option>" + albumEntity.getTitle() + "</option>");
            else
                options.add("<option>" + albumEntity.getTitle() + "</option>");
        }
        String opt = "";
        for (String s: options) {
            opt += s;
        }
        System.out.println("song id: " + songId);
        System.out.println("artist id: " + artistId + "\n");
        return opt;
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

/*        @RequestMapping(value = "/search/action", method = RequestMethod.POST)
    public String search(@RequestParam String title, @PageableDefault(value = 5, page = 0, sort = {"id"})Pageable pageable, ModelMap model) {
        //Page<SongEntity> p = model.get("song");


        List<SongEntity> songs = songRepository.findAll();
        for (int index = 0; index < songs.size(); index++)
            if(!songs.get(index).getTitle().toLowerCase().contains(title.toLowerCase()))
                songs.remove(index--);
        //cat.put("songs", songs);
        for (SongEntity s: songs) {
            System.out.println(s.getTitle() + " - " + s.getArtist());
        }
        Page<SongEntity> page = new PageImpl<SongEntity>(songs, pageable, songs.size());
        model.addAttribute("songPage", page);
        return "search";
    }
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
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("musicorganizer/src/main/webapp/WEB-INF/config/servlet-config.xml");
//        MusicMailSender ms = (MusicMailSender) context.getBean("mailSender");
//        ms.sendMail();
        return "redirect:/home/?page=0";
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @RequestMapping(value = "/addartist")
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
        albumEntity.setArtistEntity(artistEntity);
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
        System.out.println(id);
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
        System.out.println("rating = " + rating + " = " + Integer.parseInt(rating));
        songEntity.setAlbumEntity(albumEntity);
        songRepository.save(songEntity);
        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, @RequestParam("page") int page) {
        songRepository.delete(id);
        return "redirect:/home/?page=" + page;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String edit(@PathVariable("id") Long songId, @RequestParam("page") int page,
                       @ModelAttribute("editForm") Song song, ModelMap modelMap) {
        SongEntity songEntity = songRepository.findOne(songId);
        String title = song.getTitle();
        String rating = song.getRating();
        String artist = song.getArtist();
        String genre = song.getGenre();
        String album = song.getAlbum();
        String albumId = song.getAlbumId();
        String date = song.getDate();
        if (title.equals(""))
            title = songEntity.getTitle();
        if (rating == null || rating.isEmpty())
            rating = Integer.toString(songEntity.getRating());
        System.out.println("Title: " + title + "\nArtist: " + artist + "\nGenre: " + genre +
                "\nRating: " + rating + "\n");
        ErrorInput errors = new ErrorInput(songRepository, albumRepository, artistRepository);
        ArrayList<String> errmsg = errors.checkSong(title, genre, rating, album, date, artist);
        if (errmsg.size() > 0) {
            modelMap.put("inputErrors", errmsg);
            song.setTitle(title);
            song.setArtist(artist);
            song.setAlbum(album);
            song.setDate(date);
            song.setGenre(genre);
            song.setAlbumId(albumId);
            //song.setRating(rating);
            modelMap.put("editForm", song);
            System.out.println("Input has the following errors:\n" + errors.getErrorStrings(ErrorInput.PRINT));
        } else {
            songEntity.setTitle(title);
            if (!genre.equals(""))
                songEntity.setGenre(genre);
            songEntity.setRating(Integer.parseInt(rating));
            if(!album.equals(""))
                songEntity.getAlbumEntity().setTitle(album);
            if(!date.equals(""))
                songEntity.getAlbumEntity().setDate(date);
            if(!artist.equals(""))
                songEntity.getArtistEntity().setArtist(artist);
            artistRepository.save(songEntity.getArtistEntity());
            albumRepository.save(songEntity.getAlbumEntity());
            songRepository.save(songEntity);
            modelMap.put("inputErrors", null);
        }
        return "redirect:/home/?page=" + page;



//        ErrorInput errorInput = new ErrorInput(songRepository, albumRepository, artistRepository);
//        System.out.println(id);
//        AlbumEntity albumEntity = albumRepository.findById(Integer.parseInt(id));
//        List<String> errmsg = errorInput.checkSong(songTitle, genre, rating, albumEntity.getTitle(), albumEntity.getDate(), albumEntity.getArtistEntity().getArtist());
//        if(errmsg.size() > 0) {
//            modelMap.put("inputErrors", errmsg);
//            song.setId(id);
//            song.setTitle(songTitle);
//            song.setRating(rating);
//            song.setGenre(genre);
//            modelMap.put("albums", getAlbums());
//            modelMap.put("songForm", song);
//            return "add-song";
//        }
//        SongEntity songEntity = new SongEntity(songTitle, genre, Integer.parseInt(rating));
//        System.out.println("rating = " + rating + " = " + Integer.parseInt(rating));
//        songEntity.setAlbumEntity(albumEntity);
//        songRepository.save(songEntity);
//        return "redirect:/";
    }
}
