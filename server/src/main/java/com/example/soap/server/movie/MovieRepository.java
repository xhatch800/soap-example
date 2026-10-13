package com.example.soap.server.movie;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieRepository {

    private static final List<Movie> MOVIES = List.of(
            new Movie(1, "Nosferatu", 1922, "F. W. Murnau", "vampire"),
            new Movie(2, "Frankenstein", 1931, "James Whale", "monster"),
            new Movie(3, "Dracula", 1931, "Tod Browning", "vampire"),
            new Movie(4, "Psycho", 1960, "Alfred Hitchcock", "psychological"),
            new Movie(5, "The Birds", 1963, "Alfred Hitchcock", "creature"),
            new Movie(6, "Night of the Living Dead", 1968, "George A. Romero", "zombie"),
            new Movie(7, "Rosemary's Baby", 1968, "Roman Polanski", "occult"),
            new Movie(8, "The Exorcist", 1973, "William Friedkin", "supernatural"),
            new Movie(9, "The Texas Chain Saw Massacre", 1974, "Tobe Hooper", "slasher"),
            new Movie(10, "Jaws", 1975, "Steven Spielberg", "creature"),
            new Movie(11, "Carrie", 1976, "Brian De Palma", "supernatural"),
            new Movie(12, "Halloween", 1978, "John Carpenter", "slasher"),
            new Movie(13, "Dawn of the Dead", 1978, "George A. Romero", "zombie"),
            new Movie(14, "Alien", 1979, "Ridley Scott", "sci-fi"),
            new Movie(15, "The Shining", 1980, "Stanley Kubrick", "psychological"),
            new Movie(16, "The Evil Dead", 1981, "Sam Raimi", "supernatural"),
            new Movie(17, "The Thing", 1982, "John Carpenter", "sci-fi"),
            new Movie(18, "Poltergeist", 1982, "Tobe Hooper", "supernatural"),
            new Movie(19, "A Nightmare on Elm Street", 1984, "Wes Craven", "slasher"),
            new Movie(20, "The Fly", 1986, "David Cronenberg", "body horror"),
            new Movie(21, "Hellraiser", 1987, "Clive Barker", "supernatural"),
            new Movie(22, "The Silence of the Lambs", 1991, "Jonathan Demme", "psychological"),
            new Movie(23, "Scream", 1996, "Wes Craven", "slasher"),
            new Movie(24, "The Blair Witch Project", 1999, "Daniel Myrick, Eduardo Sánchez", "found footage"),
            new Movie(25, "28 Days Later", 2002, "Danny Boyle", "zombie"),
            new Movie(26, "The Descent", 2005, "Neil Marshall", "creature"),
            new Movie(27, "Let the Right One In", 2008, "Tomas Alfredson", "vampire"),
            new Movie(28, "The Babadook", 2014, "Jennifer Kent", "psychological"),
            new Movie(29, "It Follows", 2014, "David Robert Mitchell", "supernatural"),
            new Movie(30, "Get Out", 2017, "Jordan Peele", "psychological"),
            new Movie(31, "Hereditary", 2018, "Ari Aster", "occult"),
            new Movie(32, "Midsommar", 2019, "Ari Aster", "folk horror"));

    public List<Movie> findAll() {
        return MOVIES;
    }
}
