import logo from '../logo.svg';
import './css/custom_css/App.css';


import React from "react";

import "./css/bootstrap/css/bootstrap.css"
import "./css/material-components-web/material-components-web/dist/material-components-web.css"
import "./css/font-awesome/css/font-awesome-5.css"
import "./css/boxicons/css/boxicons.css"

import "./css/custom_css/Header.css";

function Dummy() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/Dummy.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

/**
 * React Component for showing header
 *
 * -> This should be reactive
 */


export class Header extends React.Component {
  render() {
    return (
        <header className="mdc-top-app-bar bg-primary ">
          <div className="mdc-top-app-bar__row">
            <section className="mdc-top-app-bar__section mdc-top-app-bar__section--align-start">
              <button className="material-icons mdc-top-app-bar__navigation-icon mdc-icon-button"
                      aria-label="Open navigation menu">
                <i className="bx bx-menu"/>
              </button>
              <span className="mdc-top-app-bar__title d-none d-md-block">Eswatini Emergency Services</span>
            </section>
            <section className="mdc-top-app-bar__section mdc-top-app-bar__section--align-end" role="toolbar">
              <button className="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                      aria-label="Favorite">
                <i className="bx bx-heart"/>
              </button>
              <button className="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                      aria-label="Search">
                <i className="bx bx-search"/>
              </button>
              <button className="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                      aria-label="Options">
                <i className="bx bx-log-in-circle"/>
              </button>
            </section>
          </div>
        </header>
    );
  };
}




export default Dummy;
