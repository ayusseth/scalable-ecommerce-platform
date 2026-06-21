import MainLayout from "../../layouts/MainLayout";

function HomePage() {
  return (
    <MainLayout>

      <section className="max-w-7xl mx-auto px-6 py-20">

        <h1 className="text-5xl font-bold mb-6">
          Welcome to Ayush Ecommerce
        </h1>

        <p className="text-xl text-gray-600 mb-8">
          Modern Ecommerce Platform built with
          Spring Boot, React, PostgreSQL and Redis.
        </p>

        <button
          className="
          bg-blue-600
          text-white
          px-6
          py-3
          rounded-lg
          hover:bg-blue-700
          "
        >
          Shop Now
        </button>

      </section>

    </MainLayout>
  );
}

export default HomePage;