import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import {
  getUsers,
  searchUsers,
  enableUser,
  disableUser,
} from "../../services/adminService";

function AdminUsersPage() {
  const [users, setUsers] = useState([]);

  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const data = await getUsers();

      setUsers(data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleSearch = async () => {
    try {
      const data =
        keyword.trim() === ""
          ? await getUsers()
          : await searchUsers(keyword);

      setUsers(data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleEnable = async (userId) => {
    await enableUser(userId);

    loadUsers();
  };

  const handleDisable = async (userId) => {
    await disableUser(userId);

    loadUsers();
  };

  return (
    <MainLayout>
      <div className="max-w-7xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          User Management
        </h1>

        <div className="flex gap-3 mb-8">
          <input
            type="text"
            placeholder="Search users..."
            value={keyword}
            onChange={(e) =>
              setKeyword(e.target.value)
            }
            className="border p-3 rounded w-full"
          />

          <button
            onClick={handleSearch}
            className="bg-blue-600 text-white px-6 rounded"
          >
            Search
          </button>
        </div>

        <div className="space-y-4">

          {users.map((user) => (

            <div
              key={user.id}
              className="border rounded p-6 flex justify-between"
            >

              <div>
                <h3 className="font-bold text-xl">
                  {user.name}
                </h3>

                <p>{user.email}</p>

                <p>
                  Verified:
                  {" "}
                  {user.emailVerified
                    ? "Yes"
                    : "No"}
                </p>

                <p>
                  Status:
                  {" "}
                  {user.enabled
                    ? "Enabled"
                    : "Disabled"}
                </p>

                <div className="mt-2 flex gap-2">
                  {user.roles.map((role) => (
                    <span
                      key={role}
                      className="
                        bg-slate-200
                        px-3
                        py-1
                        rounded-full
                        text-sm
                      "
                    >
                      {role}
                    </span>
                  ))}
                </div>
              </div>

              <div>
                {user.enabled ? (
                  <button
                    onClick={() =>
                      handleDisable(user.id)
                    }
                    className="
                      bg-red-600
                      text-white
                      px-4
                      py-2
                      rounded
                    "
                  >
                    Disable
                  </button>
                ) : (
                  <button
                    onClick={() =>
                      handleEnable(user.id)
                    }
                    className="
                      bg-green-600
                      text-white
                      px-4
                      py-2
                      rounded
                    "
                  >
                    Enable
                  </button>
                )}
              </div>

            </div>

          ))}

        </div>
      </div>
    </MainLayout>
  );
}

export default AdminUsersPage;