package com.example.firstapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserPermissionManager {

    private static UserPermissionManager instance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private UserPermissionManager() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public static synchronized UserPermissionManager getInstance() {
        if (instance == null) {
            instance = new UserPermissionManager();
        }
        return instance;
    }

    public interface PermissionCallback {
        void onResult(boolean hasPermission);
        void onError(String error);
    }

    public interface UserRoleCallback {
        void onResult(String role);
        void onError(String error);
    }

    public void checkUserPermission(String permission, PermissionCallback callback) {
        if (mAuth.getCurrentUser() == null) {
            callback.onError("משתמש לא מחובר");
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean hasPermission = documentSnapshot.getBoolean("permissions." + permission);
                        callback.onResult(hasPermission != null && hasPermission);
                    } else {
                        callback.onError("משתמש לא נמצא במסד הנתונים");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getUserRole(UserRoleCallback callback) {
        if (mAuth.getCurrentUser() == null) {
            callback.onError("משתמש לא מחובר");
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        callback.onResult(role != null ? role : "עובד");
                    } else {
                        callback.onError("משתמש לא נמצא במסד הנתונים");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void isAdmin(PermissionCallback callback) {
        getUserRole(new UserRoleCallback() {
            @Override
            public void onResult(String role) {
                callback.onResult("מנהל".equals(role));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void isEmployee(PermissionCallback callback) {
        getUserRole(new UserRoleCallback() {
            @Override
            public void onResult(String role) {
                callback.onResult("עובד".equals(role));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void isCustomer(PermissionCallback callback) {
        getUserRole(new UserRoleCallback() {
            @Override
            public void onResult(String role) {
                callback.onResult("לקוח".equals(role));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void isStaffMember(PermissionCallback callback) {
        getUserRole(new UserRoleCallback() {
            @Override
            public void onResult(String role) {
                callback.onResult("מנהל".equals(role) || "עובד".equals(role));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}