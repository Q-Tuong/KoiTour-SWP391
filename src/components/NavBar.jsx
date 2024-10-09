import React from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

function NavBar() {
	let user = JSON.parse(localStorage.getItem("user-info"));
	const navigate = useNavigate();

	function logOut() {
		localStorage.clear();
		navigate("/Login");
	}

	return (
		<Navbar collapseOnSelect expand="md" bg="dark" data-bs-theme="dark">
			<Container>
				<Navbar.Brand>
					<Link to={"/"} className="nav-link">
						KoiTripCo.
					</Link>
				</Navbar.Brand>
				<Navbar.Toggle aria-controls="navbar-nav" />
				<Navbar.Collapse id="navbar-nav">
					<Nav className="me-auto navbar-wrapper">
						<Link to={"/"} className="nav-link">
							Home
						</Link>
						<Link to={"/About"} className="nav-link">
							About
						</Link>
						<Link to={"/Guidelines"} className="nav-link">
							Guidelines
						</Link>
						<NavDropdown title="Services" id="collapsible-nav-dropdown">
							<NavDropdown.Item>
								<Link to={"/KoiList"} className="nav-link">
									Koi List
								</Link>
							</NavDropdown.Item>
							<NavDropdown.Item>
								<Link to={"/FarmList"} className="nav-link">
									Farm Site
								</Link>
							</NavDropdown.Item>
							<NavDropdown.Item>
								<Link to={"/TourList"} className="nav-link">
									Farm Tour
								</Link>
							</NavDropdown.Item>
						</NavDropdown>
						{localStorage.getItem("user-info") ? (
							//After login, it will show this.
							<>
								<NavDropdown title={user && user.username} id="collapsible-nav-dropdown">
									<NavDropdown.Item onClick={logOut}>Logout</NavDropdown.Item>
								</NavDropdown>
							</>
						) : (
							//Not login will show this
							<>
								<Link to={"/Login"} className="nav-link">
									Login
								</Link>
								<Link to={"/Register"} className="nav-link">
									Register
								</Link>
							</>
						)}
					</Nav>
				</Navbar.Collapse>
			</Container>
		</Navbar>
	);
}

export default NavBar;
