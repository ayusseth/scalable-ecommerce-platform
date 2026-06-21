import AppRoutes from "./routes/AppRoutes";

import { CartProvider }
from "./store/cartStore";

import { AuthProvider }
from "./store/authStore";

function App() {

  return (

    <AuthProvider>

      <CartProvider>

        <AppRoutes />

      </CartProvider>

    </AuthProvider>

  );

}

export default App;