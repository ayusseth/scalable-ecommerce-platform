import { useState } from "react";
import {
  useNavigate,
  useLocation,
} from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";
import { verifyOtp, sendOtp } from "../../services/authService";

function VerifyOtpPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState(location.state?.email || "");
  const [otp, setOtp] = useState("");

  const [loading, setLoading] = useState(false);

  const handleVerifyOtp = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      await verifyOtp({
        email,
        otp,
      });

      alert("Email verified successfully");

      navigate("/login");
    } catch (error) {
      console.error(error);

      alert("Invalid or expired OTP");
    } finally {
      setLoading(false);
    }
  };

  const handleResendOtp = async () => {
    try {
      await sendOtp({
        email,
      });

      alert("OTP sent successfully");
    } catch (error) {
      console.error(error);

      alert("Failed to resend OTP");
    }
  };

  return (
    <MainLayout>
      <div className="max-w-md mx-auto py-10">
        <h1 className="text-4xl font-bold mb-8">Verify OTP</h1>

        <form onSubmit={handleVerifyOtp} className="space-y-4">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border p-3 rounded"
          />

          <input
            type="text"
            placeholder="Enter OTP"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            className="w-full border p-3 rounded"
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white p-3 rounded"
          >
            {loading ? "Verifying..." : "Verify OTP"}
          </button>

          <button
            type="button"
            onClick={handleResendOtp}
            className="w-full bg-gray-600 text-white p-3 rounded"
          >
            Resend OTP
          </button>
        </form>
      </div>
    </MainLayout>
  );
}

export default VerifyOtpPage;
