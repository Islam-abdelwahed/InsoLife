package com.example.insulife;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DataBase {

    private static DataBase instance;
    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseAuth auth;

    public void createUserWithEmailAndPassword(String imgUrl, String email, String password, String userName, String phone, String type, ReturnMessage rm) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                String user_id = user.getUid();

                reference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
                HashMap<String, String> userinfo = new HashMap<>();
                userinfo.put("username", userName);
                userinfo.put("phone", phone);
                userinfo.put("imageUrl", imgUrl);
                userinfo.put("type", type);

                reference.setValue(userinfo).addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()) {
                        rm.Message("Created Successfully");
                    } else {
                        rm.Message("Not Valid username or description");
                    }
                });

            } else {
                rm.Message("Not Valid email or password");
            }
        }).addOnCanceledListener(() -> {
            rm.Message("some error occurred");
        }).addOnFailureListener(e -> {
            rm.Message("some error occurred");
        });
    }

//    public void getCarById(String Id, ReturnCar rc) {
//        reference = FirebaseDatabase.getInstance().getReference().child("Cars");
//        reference.orderByChild("Id").equalTo(Id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    reference = FirebaseDatabase.getInstance()
//                            .getReference()
//                            .child("Cars")
//                            .child(Objects.requireNonNull(data.getKey()));
//                    rc.returnCar(data.getValue(Car.class));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

    public void deleteAccount(ReturnMessage rm) {
        auth.getCurrentUser().delete().addOnCompleteListener(task -> {
           rm.Message("account unlinked");
        });
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(user.getUid()).removeValue().addOnCompleteListener(task -> {
            rm.Message("Deleted Successfully");
        }).addOnFailureListener(e -> {
            rm.Message("Some error occurred");
        });
    }

    public void finish() {
        instance=null;
        user=null;
        auth=null;
        reference=null;
    }

    public void deleteCar(String id,ReturnMessage rm) {
        reference = FirebaseDatabase.getInstance().getReference().child("Cars");
        reference.orderByChild("Id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                      FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Cars").child(Objects.requireNonNull(data.getKey())).removeValue().addOnCompleteListener(task -> {
                                rm.Message("Car has been Deleted successfully");
                            }).addOnFailureListener(e -> {
                                rm.Message(e.getMessage());
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

//    public interface RefreshCars {
//        void refreshAdapter(ArrayList<Car> c);
//
//    }

    public void getPulse(RefreshPulse rp) {
        reference = FirebaseDatabase.getInstance().getReference().child("Signals");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rp.refreshpulse(snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface RefreshPulse {
        void refreshpulse(int hb);

    }

//    public interface ReturnCar {
//        void returnCar(Car c);
//
//    }

    public interface ReturnUser {
        void returnUser(User u);

    }

    public interface ReturnMessage {
        void Message(String s);

    }

    private DataBase() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    public static DataBase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase();
                }
            }
        }
        return instance;
    }

    public static void DataBaseRefresh() {
        instance = null;
        getInstance();
    }

//    public void getAllTrips(RefreshCars r) {
//        ArrayList<Car> trips = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("Cars");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                trips.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    trips.add(snapshot1.getValue(Car.class));
//                }
//                if (!(trips.isEmpty() || trips.size() == 0)) {
//                    r.refreshAdapter(trips);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

    public void updateCarData(String Id, HashMap<String, Object> map) {
        reference = FirebaseDatabase.getInstance().getReference().child("Cars");
        reference.orderByChild("Id").equalTo(Id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    reference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Cars")
                            .child(Objects.requireNonNull(data.getKey()));
                    reference.updateChildren(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void getUserData(ReturnUser ru) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                if (user1 != null) {
                    user1.setEmail(user.getEmail());
                    user1.setId(user.getUid());
                    ru.returnUser(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateProfile(HashMap<String, Object> map) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.updateChildren(map);
    }

    public void logout() {
        auth.signOut();
    }

    public FirebaseUser getUser() {

        return this.user;
    }

    public void addCar(String name,String id,ReturnMessage rm){
        reference = FirebaseDatabase.getInstance().getReference("Cars");
        HashMap<String, Object> carInfo = new HashMap<>();
        carInfo.put("Name", name);
        carInfo.put("Location", "");
        carInfo.put("Id", id);
        carInfo.put("Status", "Free");
        carInfo.put("Battery", 0);
        carInfo.put("Heartbeat", 0);
        carInfo.put("Speed", 0);
        carInfo.put("Destination", "");
        reference.push().setValue(carInfo).addOnCompleteListener(task -> {
            rm.Message("Car has been added successfully");
        }).addOnFailureListener(e -> {
            rm.Message(e.getMessage());
        });
    }
}
