// import "./App.css";

// import ProfileForm from "./components/ProfileForm";

// function App() {
//   return (
//     <div>
//       <h1>Create Profile</h1>
//       <p>Welcome to Outty! Complete your profile below.</p>

//       <ProfileForm />
//     </div>
//   );
// }

// export default App;

import { Routes, Route } from "react-router-dom";
import RegisterForm from "./components/RegisterForm";
//import Login from "./components/Login";
import ProfileForm from "./components/ProfileForm";

function App() {
  return (
    <Routes>
      <Route path="/" element={<RegisterForm />} />
      {/* <Route path="/login" element={<Login />} /> */}
      <Route path="/profile" element={<ProfileForm />} />
    </Routes>
  );
}

export default App;
