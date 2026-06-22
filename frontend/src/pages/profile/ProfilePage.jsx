import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";
import { getCurrentUser } from "../../services/userService";

function ProfilePage() {
  const [user, setUser] = useState(null);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getCurrentUser();

        setUser(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) {
    return (
      <MainLayout>
        <div className="p-10">Loading profile...</div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-3xl mx-auto p-8">
        <h1 className="text-4xl font-bold mb-8">My Profile</h1>

        <div className="border rounded p-6 space-y-4">
          <div>
            <p className="font-semibold">Name</p>

            <p>{user.name}</p>
          </div>

          <div>
            <p className="font-semibold">Email</p>

            <p>{user.email}</p>
          </div>

          <div>
            <p className="font-semibold">Email Verified</p>

            <p>{user.emailVerified ? "Yes" : "No"}</p>
          </div>

          <div>
            <p className="font-semibold">Role</p>

            <p>{user.roles.join(", ")}</p>
          </div>
        </div>
      </div>
    </MainLayout>
  );
}

export default ProfilePage;
