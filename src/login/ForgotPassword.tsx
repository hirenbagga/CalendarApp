import AuthForm from "./AuthForm";

const ForgotPassword = () => {
  const handleReset = (data: Record<string, string>) => {
    console.log("Password Recovery:", data);
  };

  return (
    <AuthForm
      title="Forgot Password"
      fields={[{ name: "email", type: "email", placeholder: "Enter your registered email" }]}
      buttonText="Send Reset Email"
      onSubmit={handleReset}
    />
  );
};

export default ForgotPassword;
