import { useState } from "react";
// import { useNavigate } from "react-router-dom";
import { Typography, Link } from "@mui/material";
interface AuthFormProps {
    title: string;
    fields: { name: string; type: string; placeholder: string }[];
    buttonText: string;
    onSubmit: (data: Record<string, string>) => void;
}

const AuthForm = ({ title, fields, buttonText, onSubmit }: AuthFormProps) => {
    const [formData, setFormData] = useState<Record<string, string>>({});
    const [focusedField, setFocusedField] = useState<string | null>(null);
    // const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-r from-purple-100 via-pink-50 to-indigo-100">
            <div className="relative bg-white p-10 rounded-2xl shadow-2xl w-96 border border-gray-100 overflow-hidden">
                {/* Decorative elements */}
                <div className="absolute top-0 right-0 w-32 h-32 bg-gradient-to-br from-purple-200 to-pink-200 rounded-full -mr-16 -mt-16 opacity-60"></div>
                <div className="absolute bottom-0 left-0 w-20 h-20 bg-gradient-to-tr from-indigo-200 to-purple-200 rounded-full -ml-10 -mb-10 opacity-60"></div>

                <div className="relative">
                    <h2 className="text-3xl font-bold text-gray-800 mb-8 text-center">{title}</h2>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        {fields.map((field) => (
                            <div key={field.name} className="relative">
                                <input
                                    type={field.type}
                                    name={field.name}
                                    placeholder={field.placeholder}
                                    onChange={handleChange}
                                    onFocus={() => setFocusedField(field.name)}
                                    onBlur={() => setFocusedField(null)}
                                    className={`w-full px-5 py-4 border rounded-xl focus:outline-none transition-all duration-300 
                  ${focusedField === field.name
                                            ? "border-purple-500 bg-white shadow-md ring-2 ring-purple-200"
                                            : "border-gray-200 bg-gray-50 hover:border-purple-300"}`}
                                />
                            </div>
                        ))}

                        <button
                            type="submit"
                            className="w-full bg-gradient-to-r from-purple-500 to-indigo-600 text-white py-4 rounded-xl 
              font-medium shadow-lg hover:shadow-xl transition-all duration-300 
              hover:from-purple-600 hover:to-indigo-700 transform hover:-translate-y-1 mt-8"
                        >
                            {buttonText}
                        </button>
                    </form>

                    {/* Uncomment to restore the "return home" button */}
                    <div className="mt-8 text-center">
                        {/* <button 
              onClick={() => navigate("/")} 
              className="text-purple-600 hover:text-indigo-800 font-medium transition-colors flex items-center justify-center mx-auto"
            >
              <span className="mr-1">←</span> 返回首页
            </button> */}
                        <Typography variant="body2" sx={{ mt: 2 }}>
                            <Link href="/register" underline="hover">
                                Sign Up
                            </Link>
                            {" | "}
                            <Link href="/forgotpassword" underline="hover">
                                Forgot Password?
                            </Link>
                        </Typography>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuthForm;