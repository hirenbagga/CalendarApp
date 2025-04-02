import AuthForm from "../login/AuthForm";

const Login = () => {
  const handleLogin = (data: Record<string, string>) => {
    console.log("Login Information:", data);
  };

  return (
    <AuthForm
      title="Login"
      fields={[
        { name: "email", type: "email", placeholder: "Email" },
        { name: "password", type: "password", placeholder: "Password" },
      ]}
      buttonText="Login"
      onSubmit={handleLogin}
    />
  );
};

export default Login;
