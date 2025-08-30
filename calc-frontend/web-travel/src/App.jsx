import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/layout/Layout';
import HomePage from './pages/HomePage';
import AuthPage from './pages/AuthPage';
import ProfilePage from './pages/ProfilePage';
import CountriesPage from './pages/CountriesPage';
import TravelInsuranceForm from './components/insurance/TravelInsuranceForm';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/auth" element={<AuthPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/countries" element={<CountriesPage />} />
            <Route path="/countries/:countryCode" element={<CountriesPage />} />
            <Route path="/insurance" element={<TravelInsuranceForm />} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
}

export default App;
