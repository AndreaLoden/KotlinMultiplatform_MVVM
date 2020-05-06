//
//  ViewController.swift
//  iOSApp
//
//  Created by Javier Arroyo on 11/12/2019.
//  Copyright © 2019 Javier Arroyo. All rights reserved.
//

import UIKit
import SharedCode

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    // View
    @IBOutlet weak var mTableView: UITableView!
    
    private var mMovieListViewModel: MovieListViewModel!
    
    // Table View Data
    internal var mMovieList: [MovieContainer.Movie] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configView()
        initViewModel()
    }
    
    func configView() {
        mTableView.dataSource = self
        mTableView.delegate = self
    }
    
    func initViewModel() {
        mMovieListViewModel = MovieListViewModel()
        observeGitHubViewModel()
    }
    
    /****************************************************************************
     * OBSERVER VIEW MODEL
     ***************************************************************************/
    
    func observeGitHubViewModel() {
        mMovieListViewModel.mGetPopularMovieListLiveData.addObserver { (mCurrentState) in
            if (mCurrentState is SuccessGetPopularMoviesState) {
                let successState = mCurrentState as! SuccessGetPopularMoviesState
                let response = (successState.response as! Response.Success)
                let value = response.data as! [MovieContainer.Movie]
                self.onSuccessGetMovieList(list: value)
                
            } else if (mCurrentState is LoadingGetPopularMoviesState) {
                //self.mCounterLabel.text = "Loading"
            } else if (mCurrentState is ErrorGetPopularMoviesState) {
                //self.mCounterLabel.text = "ERROR"
            }
            
        }
    }
    
    func onSuccessGetMovieList(list: [MovieContainer.Movie]) {
        update(list: list)
    }
    
    /*****************************************************************************
     TABLE VIEW
     ****************************************************************************/
    internal func update(list: [MovieContainer.Movie]) {
        mMovieList.removeAll()
        mMovieList.append(contentsOf: list)
        mTableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mMovieList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "locationListCell", for: indexPath)
        let entry = mMovieList[indexPath.row]
        
        cell.textLabel?.text = entry.title
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let entryNum = mMovieList[indexPath.row].title
    }
    
    
    deinit {
        //mCounterViewModel.onCleared()
    }
    
}


