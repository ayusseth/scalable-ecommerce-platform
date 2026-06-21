import { BrowserRouter, Routes, Route } from "react-router-dom";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>

        <Route
          path="/"
          element={<h1>Home Page</h1>}
        />

        <Route
          path="/login"
          element={<h1>Login Page</h1>}
        />

        <Route
          path="/register"
          element={<h1>Register Page</h1>}
        />

        <Route
          path="/products"
          element={<h1>Products Page</h1>}
        />

        <Route
          path="/cart"
          element={<h1>Cart Page</h1>}
        />

      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;