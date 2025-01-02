package comp3350.a15.eventease.objects;

import java.util.Objects;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

public class Planner {
    private final int id;
    private final int accountId;
    private final String firstname;
    private final String lastname;
    private final String phoneNumber;
    private final String email;
    private final float rating;
    private final String bio;

    @AssistedInject
    public Planner(@Assisted("userid") int userId, @Assisted("firstname") String firstname, @Assisted("lastname") String lastname,
                   @Assisted("phone") String phone, @Assisted("email") String email, @Assisted("rating") float rating, @Assisted("bio") String bio) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phone;
        this.email = email;
        this.rating = rating;
        this.bio = bio;
        this.accountId = userId;
        this.id = hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                accountId,
                firstname,
                lastname,
                phoneNumber,
                email
        );
    }


    public int getAccountId() {
        return accountId;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getFullname() {
        return this.firstname + " " + this.lastname;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public float getRating() {
        return this.rating;
    }

    public String getBio() {
        return bio;
    }


}
