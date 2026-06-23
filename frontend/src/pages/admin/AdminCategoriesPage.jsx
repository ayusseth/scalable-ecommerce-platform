import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import {
  getCategories,
  deleteCategory,
} from "../../services/adminCategoryService";

function AdminCategoriesPage() {

  const [categories, setCategories] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {

    try {

      const data =
        await getCategories();

      setCategories(data);

    } catch (error) {

      console.error(error);

    } finally {

      setLoading(false);

    }
  };

  const handleDelete = async (id) => {

    const confirmed =
      window.confirm(
        "Delete this category?"
      );

    if (!confirmed) return;

    try {

      await deleteCategory(id);

      await loadCategories();

      alert(
        "Category deleted successfully"
      );

    } catch (error) {

      console.error(error);

      alert(
        "Failed to delete category"
      );

    }
  };

  if (loading) {

    return (
      <MainLayout>
        <div className="p-10">
          Loading...
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>

      <div className="max-w-6xl mx-auto p-10">

        <div className="flex justify-between mb-8">

          <h1 className="text-4xl font-bold">
            Categories
          </h1>

          <Link
            to="/admin/categories/new"
            className="
              bg-blue-600
              text-white
              px-4
              py-2
              rounded
            "
          >
            Create Category
          </Link>

        </div>

        <div className="space-y-4">

          {categories.map(
            (category) => (
              <div
                key={category.id}
                className="
                  border
                  rounded-lg
                  p-5
                "
              >

                <div className="flex justify-between">

                  <div>

                    <h2 className="font-bold">
                      {category.name}
                    </h2>

                    <p>
                      {category.description}
                    </p>

                  </div>

                  <div className="flex gap-3">

                    <Link
                      to={`/admin/categories/${category.id}/edit`}
                      className="
                        bg-yellow-500
                        text-white
                        px-4
                        py-2
                        rounded
                      "
                    >
                      Edit
                    </Link>

                    <button
                      onClick={() =>
                        handleDelete(
                          category.id
                        )
                      }
                      className="
                        bg-red-600
                        text-white
                        px-4
                        py-2
                        rounded
                      "
                    >
                      Delete
                    </button>

                  </div>

                </div>

              </div>
            )
          )}

        </div>

      </div>

    </MainLayout>
  );
}

export default AdminCategoriesPage;