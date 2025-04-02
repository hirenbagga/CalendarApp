import AuthForm from "../login/AuthForm";

const Register = () => {
  const handleRegister = (data: Record<string, string>) => {
    console.log("Registration Information:", data);
  };

  return (
    <AuthForm
      title="Register"
      fields={[
        { name: "email", type: "email", placeholder: "Email" },
        { name: "username", type: "text", placeholder: "Username" },
        { name: "password", type: "password", placeholder: "Password" },
        { name: "confirmPassword", type: "password", placeholder: "Confirm Password" },
      ]}
      buttonText="Register"
      onSubmit={handleRegister}
    />
  );
};

export default Register;
